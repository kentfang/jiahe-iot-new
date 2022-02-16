package com.jiahe.iot.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JiaheDeviceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiaheDeviceManagerApplication.class, args);
    }

}
