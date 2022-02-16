package com.jiahe.iot.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jiahe.iot.device.open.facade"})
public class JiaheIotUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiaheIotUserApplication.class, args);
    }

}
