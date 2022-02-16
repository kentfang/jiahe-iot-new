package com.jiahe.iot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jiahe.iot.device.open.facade"})
public class JiaheIotAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiaheIotAuthApplication.class, args);
    }

}
