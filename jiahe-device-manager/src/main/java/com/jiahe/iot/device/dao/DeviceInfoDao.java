package com.jiahe.iot.device.dao;

import com.jiahe.iot.common.model.DeviceInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class DeviceInfoDao {
    @Resource
    private MongoTemplate mongoTemplate;


    public DeviceInfo findOne(DeviceInfo deviceInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productKey").is(deviceInfo.getProductKey()));
        query.addCriteria(Criteria.where("deviceName").is(deviceInfo.getDeviceName()));
        return mongoTemplate.findOne(query, DeviceInfo.class);
    }

    public DeviceInfo findOne(String deviceName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceName").is(deviceName));
        return mongoTemplate.findOne(query, DeviceInfo.class);
    }

    public DeviceInfo add(DeviceInfo deviceInfo) {
        deviceInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return mongoTemplate.insert(deviceInfo);
    }

    public void updateDeviceSecret(DeviceInfo deviceInfo) {
        Query query = query(Criteria.where("deviceName").is(deviceInfo.getDeviceName()));
        Update update = Update.update("deviceSecret", deviceInfo.getDeviceSecret());
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

    public void updateDeviceOnline(DeviceInfo deviceInfo) {
        Query query = query(Criteria.where("deviceName").is(deviceInfo.getDeviceName()));
        Update update = Update.update("online", deviceInfo.getOnline());
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }


    public DeviceInfo findOneByDeviceId(String deviceId) {
        Query query = query(Criteria.where("deviceId").is(deviceId));
        return mongoTemplate.findOne(query, DeviceInfo.class);
    }
}
