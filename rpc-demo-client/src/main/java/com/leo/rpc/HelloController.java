package com.leo.rpc;

import org.springframework.stereotype.Component;

import com.leo.rpc.Hello;
import com.leo.rpc.HelloService;
import com.leo.rpc.WorldService;
import com.leo.rpc.annotation.RpcReference;


@Component
public class HelloController {

    @RpcReference
    private HelloService helloService;
    
    @RpcReference
    private WorldService worldService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        String world = this.worldService.world(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "world description is 222".equals(world);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
            System.out.println(worldService.world(new Hello("111", "222")));
        }
    }
}
