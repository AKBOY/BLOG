package com.chuyx.service.impl;

import com.chuyx.mapper.CategoryMapper;
import com.chuyx.pojo.po.Category;
import com.chuyx.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(int id) {
        return categoryMapper.getCategoryById(id);
    }
}
