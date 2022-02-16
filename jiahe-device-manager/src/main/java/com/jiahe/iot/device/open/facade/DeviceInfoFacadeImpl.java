package com.jiahe.iot.device.open.facade;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.model.DeviceInfo;
import com.jiahe.iot.common.model.DsInfo;
import com.jiahe.iot.device.dao.DeviceInfoDao;
import com.jiahe.iot.device.dao.DsInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DeviceInfoFacadeImpl implements DeviceInfoFacade {

    DeviceInfoDao deviceInfoDao;

    DsInfoDao dsInfoDao;

    public DeviceInfoFacadeImpl (DeviceInfoDao deviceInfoDao,DsInfoDao dsInfoDao){
        this.deviceInfoDao = deviceInfoDao;
        this.dsInfoDao = dsInfoDao;
    }

    @Override
    public DeviceInfo registerDevice(@RequestBody DeviceInfo thingRegister) {
        log.info("registerDevice:" + JSON.toJSONString(thingRegister));

        Map<String, String> dsinfoMao = new HashMap<>();
        dsinfoMao.put("deviceSecret", thingRegister.getDeviceSecret());
        dsinfoMao.put("productKey", thingRegister.getProductKey());
        dsinfoMao.put("deviceName", thingRegister.getDeviceName());
        DsInfo one = dsInfoDao.find(dsinfoMao);
        if (one == null) {
            return null;
        }

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceSecret(thingRegister.getDeviceSecret());
        deviceInfo.setProductKey(thingRegister.getProductKey());
        deviceInfo.setDeviceName(thingRegister.getDeviceName());

        deviceInfo = deviceInfoDao.findOne(DeviceInfo.builder().
                deviceName(thingRegister.getDeviceName()).productKey(thingRegister.getProductKey()).build());

        log.info("registerDevice  DeviceInfo:" + JSON.toJSONString(deviceInfo));
        if (deviceInfo == null) {
            deviceInfo = deviceInfoDao.add(DeviceInfo.builder().
                    deviceName(thingRegister.getDeviceName())
                    .productKey(thingRegister.getProductKey())
                    .deviceSecret(thingRegister.getDeviceSecret()).build());
        } else {
            if (!deviceInfo.getDeviceSecret().equals(thingRegister.getDeviceSecret())) {
                deviceInfoDao.updateDeviceSecret(DeviceInfo.builder().
                        deviceName(thingRegister.getDeviceName())
                        .productKey(thingRegister.getProductKey())
                        .deviceSecret(thingRegister.getDeviceSecret()).build());
            }
        }
        return deviceInfo;
    }

    @Override
    public DeviceInfo getDeviceInfo(@RequestBody DeviceInfo getDevice) {
        log.info("getDeviceInfo:" + JSON.toJSONString(getDevice));
        DeviceInfo one = deviceInfoDao.findOne(getDevice);
        return one;
    }

}
