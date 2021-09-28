package com.leo.rpc.spring;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.leo.rpc.annotation.RpcReference;
import com.leo.rpc.annotation.RpcService;
import com.leo.rpc.config.RpcServiceConfig;
import com.leo.rpc.extension.ExtensionLoader;
import com.leo.rpc.factory.SingletonFactory;
import com.leo.rpc.provider.ServiceProvider;
import com.leo.rpc.provider.impl.ZkServiceProviderImpl;
import com.leo.rpc.proxy.RpcClientProxy;
import com.leo.rpc.registry.ServiceDiscovery;
import com.leo.rpc.remoting.dto.RpcRequest;
import com.leo.rpc.remoting.transport.RpcRequestTransport;
import com.leo.rpc.remoting.transport.netty.client.NettyRpcClient;
import com.leo.rpc.remoting.transport.netty.server.NettyRpcServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

	private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

	private final Map<String, NettyRpcServer> serverMap = new ConcurrentHashMap<String, NettyRpcServer>();

	private final Map<String, NettyRpcClient> referenceClientMap = new ConcurrentHashMap<String, NettyRpcClient>(); // <host:port,Exchanger>

	private final ServiceDiscovery serviceDiscovery;

	public SpringBeanPostProcessor() {
		this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
	}

	@SneakyThrows
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(RpcService.class)) {
			log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
			// 先开启netty服务
			String host = InetAddress.getLocalHost().getHostAddress();
			NettyRpcServer server = serverMap.get(host);
			if (server == null) {
				server = new NettyRpcServer();
				server.start();
				serverMap.put(host, server);
			}
			//后将服务信息注册到zk上
			RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().service(bean).build();
			serviceProvider.publishService(rpcServiceConfig);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = bean.getClass();
		Field[] declaredFields = targetClass.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
			if (rpcReference != null) {
				RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().build();
				RpcRequest rpcRequest = RpcRequest.builder().interfaceName(declaredField.getGenericType().getTypeName())
						.build();
				List<InetSocketAddress> inetSocketAddressList = serviceDiscovery.lookupAllService(rpcRequest);
				for (InetSocketAddress inetSocketAddress : inetSocketAddressList) {
					RpcClientProxy rpcClientProxy = new RpcClientProxy(getClients(inetSocketAddress), rpcServiceConfig);
					Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
					declaredField.setAccessible(true);
					try {
						declaredField.set(bean, clientProxy);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bean;
	}

	private RpcRequestTransport getClients(InetSocketAddress inetSocketAddress) {
		String key = inetSocketAddress.getAddress().getHostAddress()+inetSocketAddress.getPort();
		NettyRpcClient client = referenceClientMap.get(key);
		if (client != null) {
			return client;
		}
		client = new NettyRpcClient();
		referenceClientMap.put(key, client);
		return client;
	}
}
