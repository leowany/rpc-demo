package com.leo.rpc.remoting.transport;

import com.leo.rpc.extension.SPI;
import com.leo.rpc.remoting.dto.RpcRequest;

@SPI
public interface RpcRequestTransport {
	/**
	 * send rpc request to server and get result
	 *
	 * @param rpcRequest message body
	 * @return data from server
	 */
	Object sendRpcRequest(RpcRequest rpcRequest);
}
