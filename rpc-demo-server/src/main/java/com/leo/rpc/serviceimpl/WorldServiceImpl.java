package com.leo.rpc.serviceimpl;


import com.leo.rpc.Hello;
import com.leo.rpc.WorldService;
import com.leo.rpc.annotation.RpcService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RpcService
public class WorldServiceImpl implements WorldService {

    static {
        System.out.println("WorldServiceImpl被创建");
    }

    @Override
    public String world(Hello hello) {
        log.info("WorldServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("WorldServiceImpl返回: {}.", result);
        return result;
    }
}
