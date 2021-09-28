package com.leo.rpc.registry;

import java.net.InetSocketAddress;
import java.util.List;

import com.leo.rpc.extension.SPI;
import com.leo.rpc.remoting.dto.RpcRequest;

@SPI
public interface ServiceDiscovery {
	/**
	 * lookup service by rpcServiceName
	 *
	 * @param rpcRequest rpc service pojo
	 * @return service address
	 */
	InetSocketAddress lookupService(RpcRequest rpcRequest);

	List<InetSocketAddress> lookupAllService(RpcRequest rpcRequest);
}
