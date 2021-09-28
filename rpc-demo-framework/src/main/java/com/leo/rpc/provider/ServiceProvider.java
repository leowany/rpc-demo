package com.leo.rpc.provider;

import com.leo.rpc.config.RpcServiceConfig;

public interface ServiceProvider {

	/**
	 * @param rpcServiceName rpc service name
	 * @return service object
	 */
	Object getService(String rpcServiceName);

	/**
	 * @param rpcServiceConfig rpc service related attributes
	 */
	void publishService(RpcServiceConfig rpcServiceConfig);

}
