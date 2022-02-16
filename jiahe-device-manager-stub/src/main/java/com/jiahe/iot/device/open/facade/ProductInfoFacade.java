package com.jiahe.iot.device.open.facade;

import com.jiahe.iot.common.model.DsInfo;
import com.jiahe.iot.common.model.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/ProductInfoFacade")
public interface ProductInfoFacade {
    @RequestMapping("/product/add")
    public ProductInfo addProduct( ProductInfo productInfo);

    @RequestMapping("/product/findbykey")
    public ProductInfo getProduct( String productKey);

    @RequestMapping("/product/createds/")
    public List<DsInfo> createDs( Map<String,String> body);
}
