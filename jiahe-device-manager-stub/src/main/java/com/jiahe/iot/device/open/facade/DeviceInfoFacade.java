package com.jiahe.iot.device.open.facade;

import com.jiahe.iot.common.model.DeviceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/DeviceInfoFacade")
public interface DeviceInfoFacade {

    @RequestMapping("/device/register/")
    public DeviceInfo registerDevice(@RequestBody DeviceInfo thingRegister);


    @RequestMapping("/device/get/")
    public DeviceInfo getDeviceInfo(@RequestBody DeviceInfo getDevice);
}
