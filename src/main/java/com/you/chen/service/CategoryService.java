package com.you.chen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.you.chen.pojo.Category;

public interface CategoryService  extends IService<Category> {

    public void remove(Long id);
}
