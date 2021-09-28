package com.leo.rpc.loadbalance;

import com.leo.rpc.extension.SPI;
import com.leo.rpc.remoting.dto.RpcRequest;

import java.util.List;

@SPI
public interface LoadBalance {
	/**
	 * Choose one from the list of existing service addresses list
	 *
	 * @param serviceAddresses Service address list
	 * @return target service address
	 */
	String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);
}
