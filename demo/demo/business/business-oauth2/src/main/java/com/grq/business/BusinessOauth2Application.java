package com.grq.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients   //Feign 客户端
@EnableDiscoveryClient //启用发现客户端
public class BusinessOauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(BusinessOauth2Application.class,args);
    }
}
