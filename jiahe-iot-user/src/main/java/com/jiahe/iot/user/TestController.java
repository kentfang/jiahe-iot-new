package com.jiahe.iot.user;

import com.jiahe.iot.device.open.facade.DsFacade;
import com.jiahe.iot.device.open.facade.TestFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    TestFacade iTestFacade;

    @Autowired
    DsFacade DsFacade;
    @RequestMapping("/test")
    public String test(){
        Map<String,String> map = new HashMap();
        DsFacade.getDsInfo(map);
       return iTestFacade.getTest();
    }

}
