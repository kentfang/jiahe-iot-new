package com.jiahe.iot.device.dao;


import com.jiahe.iot.common.model.ProductInfo;
import com.jiahe.iot.common.util.UniqueIdUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Repository
public class ProductInfoDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public ProductInfo add(ProductInfo productInfo) {
        productInfo.setProductId(UniqueIdUtil.newProductId());
        productInfo.setProductKey(UniqueIdUtil.newRamdomString(6));
        productInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return mongoTemplate.insert(productInfo);
    }

    public ProductInfo findOne(ProductInfo productInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productKey").is(productInfo.getProductKey()));
        query.addCriteria(Criteria.where("categoryId").is(productInfo.getCategoryId()));
        return mongoTemplate.findOne(query, ProductInfo.class);
    }

    public ProductInfo findOne(String productKey) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productKey").is(productKey));
        return mongoTemplate.findOne(query, ProductInfo.class);
    }


    public List<ProductInfo> getAll() {
        List<ProductInfo> all = mongoTemplate.findAll(ProductInfo.class);
        return all;
    }

    public void update() {

    }

}
