package com.leo.rpc.serviceimpl;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leo.rpc.spring.EnableRpc;

@SpringBootApplication(scanBasePackages = {"com.leo"})
@EnableRpc
public class NettyServerMain {
	public static void main(String[] args) {
		SpringApplication.run(NettyServerMain.class, args);
		System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + "example-server started!");
	}
}
