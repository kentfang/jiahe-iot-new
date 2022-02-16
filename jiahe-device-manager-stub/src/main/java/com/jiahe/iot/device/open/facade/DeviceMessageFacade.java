package com.jiahe.iot.device.open.facade;

import com.jiahe.iot.common.bean.DeviceMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/DeviceMessageFacade")
public interface DeviceMessageFacade {

    @RequestMapping("/send")
    public DeviceMessage sendDataToManager(@RequestBody DeviceMessage deviceMessage);
}
