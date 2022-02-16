package com.jiahe.iot.device.open.facade;

import com.jiahe.iot.common.bean.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Component
@FeignClient("jiahe-device-manager")
@RequestMapping(value = "/jiahe-device-manager/facade/CategoryFacade")
public interface CategoryFacade {

    @RequestMapping(value = "/category/get/{categoryId}", method = RequestMethod.GET)
    public Category getCategory(@PathVariable() String categoryId);

    @RequestMapping(value = "/category/getall/", method = RequestMethod.GET)
    public List<Category> getAllCategory();

    @RequestMapping(value = "/category/add/")
    public Category addCategory(@RequestBody Category category);

}
