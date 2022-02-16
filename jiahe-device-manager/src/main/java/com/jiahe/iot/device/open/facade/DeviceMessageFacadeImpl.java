package com.jiahe.iot.device.open.facade;

import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.bean.DeviceMessage;
import com.jiahe.iot.common.model.DeviceInfo;
import com.jiahe.iot.device.dao.DeviceInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class DeviceMessageFacadeImpl implements DeviceMessageFacade {

    DeviceInfoDao deviceInfoDao;

    public DeviceMessageFacadeImpl(DeviceInfoDao deviceInfoDao){
        this.deviceInfoDao = deviceInfoDao;
    }

    @Override
    public DeviceMessage sendDataToManager(@RequestBody DeviceMessage deviceMessage) {
        log.info("sendDataToManager:" + JSON.toJSONString(deviceMessage));
        String deviceName = deviceMessage.getDeviceName();
        DeviceInfo deviceInfo = deviceInfoDao.findOne(deviceName);
        if (deviceInfo == null) {
            return null;
        }
        deviceMessage.setProductKey(deviceInfo.getProductKey());
        switch (deviceMessage.getMessageType()) {
            case deviceEventType:

                break;
            case deviceOnlineType:
//            {"deviceName":"gm251ygitq26su63","identifier":"online","messageType":"deviceOnlineType",
//                    "requestId":"RID2022010419100410000001","success":false,"value":{}}
                deviceInfo.setOnline(deviceMessage.getIdentifier());
                deviceInfoDao.updateDeviceOnline(deviceInfo);
                break;
            case devicePropertyType:
                Map<String, Object> state = deviceInfo.getState();
                Map<String, Object> value = deviceMessage.getValue();
                Set<Map.Entry<String, Object>> entries = value.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    state.put(next.getKey(), next.getValue());
                }
                deviceInfoDao.updateDeviceSecret(deviceInfo);
                break;
        }
        return deviceMessage;
    }
}
