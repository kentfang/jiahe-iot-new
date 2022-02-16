package com.jiahe.iot.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiahe.iot.auth.config.IMqttSender;
import com.jiahe.iot.auth.open.facade.MqttAgentFacade;
import com.jiahe.iot.common.bean.DeviceMessage;
import com.jiahe.iot.common.bean.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class MqttAgentController implements MqttAgentFacade {

    @Autowired
    IMqttSender iMqttSender;

    /**
     * 平台下行接口
     *
     * @param deviceMessage
     * @return
     */
    @Override
    public Response sendDataTo(@RequestBody DeviceMessage deviceMessage) {
        log.info("MqttAgentController:" + JSON.toJSONString(deviceMessage));
        StringBuilder topic = new StringBuilder();
        JSONObject payloyd = new JSONObject();
        switch (deviceMessage.getMessageType()) {
            case deviceEventType:
                break;
            case deviceOnlineType:
                break;
            case deviceSeviceType:
//                /sys/{productKey}/{deviceName}/thing/service/{tsl.service.identifier}/invoke
                topic.append("/sys/");
                topic.append(deviceMessage.getProductKey());
                topic.append("/");
                topic.append(deviceMessage.getDeviceName());
                topic.append("/thing/service/");
                topic.append(deviceMessage.getIdentifier());
                topic.append("/invoke");
                payloyd.put("id", deviceMessage.getRequestId());
                payloyd.put("version", "1.0");
                if (deviceMessage.getValue() == null) {
                    payloyd.put("params", new HashMap<>());
                } else {
                    payloyd.put("params", deviceMessage.getValue());
                }
                break;
            case devicePropertyType:
                break;
        }
        iMqttSender.sendToMqtt(topic.toString(), payloyd.toJSONString());
        return Response.builder().code(200).data(topic.toString()).build();
    }
}
