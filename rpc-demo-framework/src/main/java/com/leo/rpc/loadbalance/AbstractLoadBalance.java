package com.leo.rpc.loadbalance;

import com.leo.rpc.remoting.dto.RpcRequest;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
	@Override
	public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
		if (serviceAddresses == null || serviceAddresses.size() == 0) {
			return null;
		}
		if (serviceAddresses.size() == 1) {
			return serviceAddresses.get(0);
		}
		return doSelect(serviceAddresses, rpcRequest);
	}

	protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);

}
