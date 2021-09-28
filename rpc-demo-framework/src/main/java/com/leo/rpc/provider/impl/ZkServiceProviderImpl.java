package com.leo.rpc.provider.impl;

import com.leo.rpc.config.RpcServiceConfig;
import com.leo.rpc.enums.RpcErrorMessageEnum;
import com.leo.rpc.exception.RpcException;
import com.leo.rpc.extension.ExtensionLoader;
import com.leo.rpc.provider.ServiceProvider;
import com.leo.rpc.registry.ServiceRegistry;
import com.leo.rpc.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

	/**
	 * key: rpc service name(interface name + version + group) value: service object
	 */
	private final Map<String, Object> serviceMap;
	private final ServiceRegistry serviceRegistry;

	public ZkServiceProviderImpl() {
		serviceMap = new ConcurrentHashMap<>();
		serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
	}

	@Override
	public Object getService(String rpcServiceName) {
		Object service = serviceMap.get(rpcServiceName);
		if (null == service) {
			throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
		}
		return service;
	}

	@Override
	public void publishService(RpcServiceConfig rpcServiceConfig) {
		try {
			String host = InetAddress.getLocalHost().getHostAddress();
			String rpcServiceName = rpcServiceConfig.getRpcServiceName();
			if (serviceMap.get(rpcServiceName) == null) {
				serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),
						new InetSocketAddress(host, NettyRpcServer.PORT));
				log.info("Add service: {} and interfaces:{}", rpcServiceName,
						rpcServiceConfig.getService().getClass().getInterfaces());
				serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
			}
		} catch (UnknownHostException e) {
			log.error("occur exception when getHostAddress", e);
		}
	}

}
