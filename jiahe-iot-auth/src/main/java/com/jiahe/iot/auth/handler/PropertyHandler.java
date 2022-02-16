package com.jiahe.iot.auth.handler;

import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.bean.DeviceMessage;
import com.jiahe.iot.common.bean.DeviceMessageType;
import com.jiahe.iot.common.bean.Request;
import com.jiahe.iot.common.bean.Response;
import com.jiahe.iot.common.constant.ResultCode;
import com.jiahe.iot.common.util.UniqueIdUtil;
import com.jiahe.iot.device.open.facade.DeviceMessageFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@TopicHandler
@RestController
public class PropertyHandler {

    @Autowired
    DeviceMessageFacade deviceMessageFacade;

    @RequestMapping("/sys/{productKey}/{deviceName}/thing/property/up_raw")
    public Response<?> eventPost(
            @PathVariable("productKey") String productKey,
            @PathVariable("deviceName") String deviceName,
            @RequestBody EventHandler.EventRequest request) {
        log.info("PropertyHandler request:" + JSON.toJSONString(request));
        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setRequestId(UniqueIdUtil.newRequestId());
        deviceMessage.setMessageType(DeviceMessageType.devicePropertyType);
        deviceMessage.setProductKey(productKey);
        deviceMessage.setDeviceName(deviceName);
        deviceMessage.setValue(request.getParams());
        deviceMessage = deviceMessageFacade.sendDataToManager(deviceMessage);
        return Response.builder().code(ResultCode.suc).id(request.getId()).build();
    }


    static class EventRequest extends Request<Map<String, Object>> {

    }
}
