package com.jiahe.iot.auth.controller;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.auth.config.IMqttSender;
import com.jiahe.iot.common.bean.*;
import com.jiahe.iot.common.model.DeviceInfo;
import com.jiahe.iot.common.model.UserInfo;
import com.jiahe.iot.common.util.JwtUtils;
import com.jiahe.iot.common.util.UniqueIdUtil;
import com.jiahe.iot.device.open.facade.DeviceMessageFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class AuthAclController {



    @Autowired
    IMqttSender iMqttSender;
    @Autowired
    DeviceMessageFacade deviceMessageFacade;



    @RequestMapping(value = "/mqtt/auth", method = RequestMethod.POST)
    public void auth(@RequestBody DeviceAuth data, HttpServletResponse response) throws Exception {
        log.info("DeviceAuth:" + JSON.toJSONString(data));
        if (data.getClientid().startsWith("jiahe")) {
            response.setStatus(200);
            return;
        }
        String clientId = data.getClientid();
        String cli[] = clientId.split("_");
        if (cli.length != 3) {
            response.setStatus(400);
            return;
        }
        String cliNm = cli[0];
        String version = cli[1];
        String type = cli[2];
        if (!cliNm.equals(data.getUsername())) {
            response.setStatus(400);
            return;
        }
        if (!type.equals("1") && !type.equals("2")) {
            response.setStatus(400);
            return;
        }

        String accessToken = data.getPassword();
        String s = JwtUtils.verifyToken(accessToken);
        if (s == null) {
            response.setStatus(400);
            return;
        }
        TokenClient tokenClient = JSON.parseObject(s, TokenClient.class);
        if (tokenClient.getType() == 1) {
            DeviceInfo deviceInfo = JSON.parseObject(tokenClient.getObject(), DeviceInfo.class);
            if (!deviceInfo.getDeviceName().equals(data.getUsername())) {
                response.setStatus(400);
                return;
            }
        } else {
            UserInfo userInfo = JSON.parseObject(tokenClient.getObject(), UserInfo.class);
            if (!userInfo.getUserName().equals(data.getUsername())) {
                response.setStatus(400);
                return;
            }
        }
    }

    @RequestMapping(value = "/mqtt/acl", method = RequestMethod.POST)
    public void acl(@RequestBody AclDeviceBean aclDeviceBean, HttpServletResponse response) {
        log.info("AclDeviceBean:" + JSON.toJSONString(aclDeviceBean));
    }

    @RequestMapping(value = "/mqtt/status", method = RequestMethod.POST)
    public void status(@RequestBody EmqxCallbackBean emqxCallbackBean) {
        log.info("emqxCallbackBean:" + JSON.toJSONString(emqxCallbackBean));
        if (emqxCallbackBean.getUsername().equals("jiahe")) {
            return;
        }
        if (emqxCallbackBean.getClientid() == null || emqxCallbackBean.getAction() == null) {
            return;
        }

        /**
         * 如果此处是 客户端 连接，，
         */
        String userName = emqxCallbackBean.getUsername();
        if (userName.length() != 16 || userName.contains("@")) {
            //这里暂时认为 是 手机app 或者其他客户端连接
            return;
        }

        String value = emqxCallbackBean.getAction().equals("client_connected") ? "online" : "offline";

        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setRequestId(UniqueIdUtil.newRequestId());
        deviceMessage.setMessageType(DeviceMessageType.deviceOnlineType);
        deviceMessage.setIdentifier(value);
        deviceMessage.setDeviceName(emqxCallbackBean.getUsername());

        DeviceMessage deviceMessage2 = deviceMessageFacade.sendDataToManager(deviceMessage);

        String topic = "/sys/" + deviceMessage2.getProductKey() + "/" + deviceMessage2.getDeviceName() + "/thing/event/" + value + "/post";

        MqttPayLoad payLoad = new MqttPayLoad();
        payLoad.setId(deviceMessage2.getRequestId());
        payLoad.setVersion("1.0");
        Map<String, Object> values = new HashMap<>();
        values.put("value", value);
        values.put("time", System.currentTimeMillis());
        Map<String, Object> params = new HashMap<>();
        params.put("onlineState", values);
        payLoad.setParams(params);

        iMqttSender.sendToMqtt(topic, JSON.toJSONString(payLoad));
    }


    @RequestMapping(value = "/mqtt/test", method = RequestMethod.POST)
    public Response test() {
        return Response.builder().code(200).message("成功").build();
    }
}
