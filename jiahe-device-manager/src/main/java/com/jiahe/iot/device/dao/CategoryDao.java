package com.jiahe.iot.device.dao;


import com.jiahe.iot.common.bean.Category;
import com.jiahe.iot.common.util.UniqueIdUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CategoryDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public Category find(String categorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("categorId").is(categorId));
        return mongoTemplate.findOne(query, Category.class);
    }

    public List<Category> getAllCategory() {
        return mongoTemplate.findAll(Category.class);
    }

    public Category add(Category category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("categoryName").is(category.getCategoryName()));
        Category one = mongoTemplate.findOne(query, Category.class);
        if (one != null) {
            return null;
        } else {
            category.setCategoryId(UniqueIdUtil.newRamdomString(5));
            Category insert = mongoTemplate.insert(category);
            return insert;
        }
    }
}
