package com.jiahe.iot.device.open.facade;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestFacadeImpl implements TestFacade{

    @Override
    public String getTest() {
        return "123456789";
    }

}
