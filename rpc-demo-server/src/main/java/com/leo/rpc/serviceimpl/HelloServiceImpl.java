package com.leo.rpc.serviceimpl;

import com.leo.rpc.Hello;
import com.leo.rpc.HelloService;
import com.leo.rpc.annotation.RpcService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RpcService
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
