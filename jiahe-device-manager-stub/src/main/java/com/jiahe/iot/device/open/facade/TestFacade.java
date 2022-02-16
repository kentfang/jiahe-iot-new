package com.jiahe.iot.device.open.facade;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/ITestFacade")
public interface TestFacade {

    @RequestMapping(value = "/getTest")
    public String getTest();
}
