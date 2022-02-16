package com.jiahe.iot.device.open.facade;


import com.alibaba.fastjson.JSON;
import com.jiahe.iot.common.bean.Category;
import com.jiahe.iot.common.constant.CacheKeys;
import com.jiahe.iot.device.component.RedisCacheManager;
import com.jiahe.iot.device.dao.CategoryDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RestController
public class CategoryFacadeImpl implements CategoryFacade {


    RedisCacheManager redisCacheManager;

    CategoryDao categoryDao;

    public CategoryFacadeImpl(RedisCacheManager redisCacheManager,CategoryDao categoryDao){
        this.redisCacheManager = redisCacheManager;
        this.categoryDao = categoryDao;
    }

    @Override
    public Category getCategory(@PathVariable() String categoryId) {
        return categoryDao.find(categoryId);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryDao.getAllCategory();
    }

    @Override
    public Category addCategory(@RequestBody Category category) {
        Category add = categoryDao.add(category);
        if (add != null) {
            redisCacheManager.set(CacheKeys.getCategoryThings(add.getCategoryId()), JSON.toJSONString(add));
        }
        return add;
    }
    @PostConstruct
    public void freshCategoryThings() {
        List<Category> all = categoryDao.getAllCategory();
        for (Category category : all) {
            redisCacheManager.set(CacheKeys.getCategoryThings(category.getCategoryId()), JSON.toJSONString(category));
        }
    }
}
