package com.jiahe.iot.device.dao;


import com.jiahe.iot.common.model.DsInfo;
import com.jiahe.iot.common.util.UniqueIdUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class DsInfoDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public List<DsInfo> get(int count, String productKey) {
        List<DsInfo> dsInfos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DsInfo dsInfo = new DsInfo();
            String ds = "";
            String dn = "";
            while (true) {
                ds = UniqueIdUtil.newRamdomString(16);
                Query query = new Query();
                query.addCriteria(Criteria.where("deviceSecret").is(ds));
                DsInfo one = mongoTemplate.findOne(query, DsInfo.class);
                if (one != null) {
                    continue;
                } else {
                    break;
                }
            }

            while (true) {
                dn = UniqueIdUtil.newRamdomString(16);
                Query query = new Query();
                query.addCriteria(Criteria.where("deviceName").is(ds));
                DsInfo one = mongoTemplate.findOne(query, DsInfo.class);
                if (one != null) {
                    continue;
                } else {
                    break;
                }
            }

            dsInfo.setProductKey(productKey);
            dsInfo.setDeviceSecret(ds);
            dsInfo.setCreateTime(new Date(System.currentTimeMillis()));
            dsInfo.setDeviceName(dn);
            dsInfos.add(dsInfo);

            mongoTemplate.save(dsInfo);
        }
        return dsInfos;
    }


    public DsInfo find(Map<String, String> maps) {
        Query query = new Query();
        Iterator<Map.Entry<String, String>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            query.addCriteria(Criteria.where(next.getKey()).is(next.getValue()));
        }
        return mongoTemplate.findOne(query, DsInfo.class);
    }
}
