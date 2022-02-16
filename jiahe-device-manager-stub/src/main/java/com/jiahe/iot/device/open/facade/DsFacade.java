package com.jiahe.iot.device.open.facade;


import com.jiahe.iot.common.model.DsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/DsFacade")
public interface DsFacade {
    @RequestMapping(value = "/ds/get/")
    public DsInfo getDsInfo(@RequestBody Map<String, String> getdsinfo);
}
