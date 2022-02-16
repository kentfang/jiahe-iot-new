package com.jiahe.iot.auth.open.facade;

import com.jiahe.iot.common.bean.DeviceMessage;
import com.jiahe.iot.common.bean.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("jiahe-auth")
@RequestMapping(value = "/jiahe-auth/auth/facade/MqttAgentFacade")
public interface MqttAgentFacade {
    @RequestMapping("/mqtt/send")
    public Response sendDataTo(DeviceMessage deviceMessage);
}
