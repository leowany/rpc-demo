package com.leo.rpc.registry.zk;

import com.leo.rpc.enums.RpcErrorMessageEnum;
import com.leo.rpc.exception.RpcException;
import com.leo.rpc.extension.ExtensionLoader;
import com.leo.rpc.loadbalance.LoadBalance;
import com.leo.rpc.registry.ServiceDiscovery;
import com.leo.rpc.registry.zk.util.CuratorUtils;
import com.leo.rpc.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import com.google.common.collect.Lists;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
	private final LoadBalance loadBalance;

	public ZkServiceDiscoveryImpl() {
		this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
	}

	@Override
	public InetSocketAddress lookupService(RpcRequest rpcRequest) {
		String rpcServiceName = rpcRequest.getRpcServiceName();
		CuratorFramework zkClient = CuratorUtils.getZkClient();
		List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
		if (serviceUrlList == null || serviceUrlList.size() == 0) {
			throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
		}
		// load balancing
		String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
		log.info("Successfully found the service address:[{}]", targetServiceUrl);
		String[] socketAddressArray = targetServiceUrl.split(":");
		String host = socketAddressArray[0];
		int port = Integer.parseInt(socketAddressArray[1]);
		return new InetSocketAddress(host, port);
	}

	@Override
	public List<InetSocketAddress> lookupAllService(RpcRequest rpcRequest) {
		String rpcServiceName = rpcRequest.getRpcServiceName();
		CuratorFramework zkClient = CuratorUtils.getZkClient();
		List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
		if (serviceUrlList == null || serviceUrlList.size() == 0) {
			throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
		}
		List<InetSocketAddress> list = Lists.newArrayList();
		for (String targetServiceUrl : serviceUrlList) {
			String[] socketAddressArray = targetServiceUrl.split(":");
			String host = socketAddressArray[0];
			int port = Integer.parseInt(socketAddressArray[1]);
			list.add(new InetSocketAddress(host, port));
		}
		return list;
	}

}
