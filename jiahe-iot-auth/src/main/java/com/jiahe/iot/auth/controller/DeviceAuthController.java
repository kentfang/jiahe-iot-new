package com.jiahe.iot.auth.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiahe.iot.auth.component.RedisCacheManager;
import com.jiahe.iot.auth.config.IMqttSender;
import com.jiahe.iot.common.bean.*;
import com.jiahe.iot.common.constant.CacheKeys;
import com.jiahe.iot.common.constant.ResultCode;
import com.jiahe.iot.common.model.DeviceInfo;
import com.jiahe.iot.common.util.JwtUtils;
import com.jiahe.iot.device.open.facade.DeviceInfoFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import static com.jiahe.iot.common.util.JwtUtils.CALENDAR_FIELD;
import static com.jiahe.iot.common.util.JwtUtils.CALENDAR_INTERVAL;


@Slf4j
@RestController
public class DeviceAuthController {

    @Autowired
    RedisCacheManager redisCacheManager;
    @Autowired
    IMqttSender iMqttSender;

    @Autowired
    DeviceInfoFacade deviceInfoFacade;

    @RequestMapping(value = "/d/v1/thing_register", method = RequestMethod.POST)
    public Response thing_register(@RequestBody ThingRegister thingRegister) throws Exception {
        log.info("thing_register:" + JSON.toJSONString(thingRegister));

        if (thingRegister.getDeviceName() == null ||
                thingRegister.getDeviceSecret() == null ||
                thingRegister.getProductKey() == null || thingRegister.getTimeStamp() == null) {
            return Response.builder().code(ResultCode.fail).message("参数缺失").build();
        }


        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceSecret(thingRegister.getDeviceSecret());
        deviceInfo.setProductKey(thingRegister.getProductKey());
        deviceInfo.setDeviceName(thingRegister.getDeviceName());

        deviceInfo = deviceInfoFacade.registerDevice(deviceInfo);

        if (deviceInfo != null) {
            if (thingRegister.getBindCode() != null) {
                String user = redisCacheManager.get(CacheKeys.getBindeCodeKey(thingRegister.getBindCode()));
                if (user != null && !"".equals(user)) {
                    JSONObject userObject = JSON.parseObject(user);
                    String topic = "/bind/" + userObject.getString("userId") + "/" +
                            deviceInfo.getProductKey() + "/" + deviceInfo.getDeviceName() + "/" + thingRegister.getBindCode() + "/bindcode/";
                    iMqttSender.sendToMqtt(topic, JSON.toJSONString(deviceInfo));
                }
            }
            ThingRegisterResult result = new ThingRegisterResult();
            result.setDeviceName(deviceInfo.getDeviceName());
            result.setLoadBalance("http://47.119.195.237:80/auth//d/v1/thing_login");
            return Response.builder().code(ResultCode.suc).message("")
                    .data(result).build();
        } else {
            return Response.builder().code(ResultCode.suc).message("三元组不匹配")
                    .data("").build();
        }
    }

    @RequestMapping(value = "/d/v1/thing_login", method = RequestMethod.POST)
    public Response thing_login(@RequestBody ThingLogin thingLogin) throws Exception {
        log.info("thing_login:" + JSON.toJSONString(thingLogin));
        if (
                thingLogin.getDeviceName() == null ||
                        thingLogin.getDeviceSecret() == null ||
                        thingLogin.getProductKey() == null) {
            return Response.builder().code(ResultCode.fail).message("参数缺失").build();
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceSecret(thingLogin.getDeviceSecret());
        deviceInfo.setProductKey(thingLogin.getProductKey());
        deviceInfo.setDeviceName(thingLogin.getDeviceName());

        DeviceInfo oneByDeviceId = deviceInfoFacade.getDeviceInfo(deviceInfo);

        if (oneByDeviceId != null) {
            if (oneByDeviceId.getDeviceSecret().equals(thingLogin.getDeviceSecret()) && oneByDeviceId.getProductKey().equals(thingLogin.getProductKey())) {
                Calendar time = Calendar.getInstance();
                time.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
                Date expireTime = time.getTime();
                TokenClient tokenClient = new TokenClient();
                tokenClient.setType(1);
                tokenClient.setObject(JSON.toJSONString(deviceInfo));
                String accessToken = JwtUtils.createToken(
                        tokenClient
                        , expireTime);
                String expiryDate = expireTime.getTime() + "";
                String loadBalance = "tcp://47.119.195.237:1883";
                ThingLoginResult result = new ThingLoginResult();
                result.setAccessToken(accessToken);
                result.setExpiryDate(expiryDate);
                result.setLoadBalance(loadBalance);
                result.setLength(accessToken.getBytes(StandardCharsets.UTF_8).length);
                redisCacheManager.set(CacheKeys.getTokenKey(accessToken), JSON.toJSONString(oneByDeviceId), CALENDAR_INTERVAL * 60);

                return Response.builder().code(ResultCode.suc).message("")
                        .data(result).build();
            } else {
                return Response.builder().code(ResultCode.fail).message("参数异常").build();
            }
        } else {
            return Response.builder().code(ResultCode.device_id_is_not_err).message("err").build();
        }

    }
}
