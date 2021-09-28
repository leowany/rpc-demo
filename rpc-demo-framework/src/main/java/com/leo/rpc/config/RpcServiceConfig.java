package com.leo.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceConfig {
	/**
	 * target service
	 */
	private Object service;

	public String getRpcServiceName() {
		return this.getServiceName();
	}

	public String getServiceName() {
		return this.service.getClass().getInterfaces()[0].getCanonicalName();
	}
}
