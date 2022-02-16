package com.jiahe.iot.device.open.facade;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.constant.CacheKeys;
import com.jiahe.iot.common.model.DsInfo;
import com.jiahe.iot.common.model.ProductInfo;
import com.jiahe.iot.device.component.RedisCacheManager;
import com.jiahe.iot.device.dao.DsInfoDao;
import com.jiahe.iot.device.dao.ProductInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductInfoFacadeImpl implements ProductInfoFacade {

    @Autowired
    ProductInfoDao productInfoDao;

    @Autowired
    DsInfoDao dsInfoDao;

    @Autowired
    RedisCacheManager redisCacheManager;

    @Override
    public ProductInfo addProduct(@RequestBody ProductInfo productInfo) {
        if (StringUtils.isAllEmpty(productInfo.getProductCode())
                || StringUtils.isAllEmpty(productInfo.getCategoryId())
                || StringUtils.isAllEmpty(productInfo.getProductName())
                || StringUtils.isAllEmpty(productInfo.getNetworking())) {
            return null;
        }
        if (productInfoDao.findOne(productInfo) != null) {
            return null;
        }
        productInfo = productInfoDao.add(productInfo);
        redisCacheManager.set(CacheKeys.getProductThings(productInfo.getProductKey()), JSON.toJSONString(productInfo));
        return productInfo;
    }

    @Override
    public ProductInfo getProduct(String productKey) {
        log.info("productKey:" + JSON.toJSONString(productKey));
        ProductInfo one = productInfoDao.findOne(productKey);
        return one;
    }

    @Override
    public List<DsInfo> createDs(Map<String,String> body) {
        log.info("createDs:" + JSON.toJSONString(body));
        int count = 1;
        if (body.containsKey("count")) {
            count = Integer.valueOf(body.get("count")) ;
        }
        if (!body.containsKey("productKey")) {
            return null;
        }
        String productKey = body.get("productKey");
        return dsInfoDao.get(count, productKey);
    }

    @PostConstruct
    @RequestMapping("/product/fresh/")
    public void freshProductThings() {
        List<ProductInfo> all = productInfoDao.getAll();
        for (ProductInfo productInfo : all) {
            redisCacheManager.set(CacheKeys.getProductThings(productInfo.getProductKey()), JSON.toJSONString(productInfo));
        }
    }
}
