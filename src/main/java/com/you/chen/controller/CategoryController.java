package com.you.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.you.chen.common.Result;
import com.you.chen.pojo.Category;
import com.you.chen.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /*
    *
    *  添加菜品分类或菜品套餐
    *
    * */
    @PostMapping
    public Result save(@RequestBody Category category){
      categoryService.save(category);
      return Result.success("添加菜品分类成功");
    }
    /*
    *  菜品分类展示功能
    *
    * */
    @GetMapping("/page")
    public Result page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>();
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }
    /*
    *
    *   删除菜品分类功能
    *
    * */
    @DeleteMapping
    public Result delete(Long id){

        categoryService.remove(id);
        return Result.success("删除菜品分类成功");
    }
    /*
    *
    *   修改菜品分类功能
    *
    * */
    @PutMapping
    public Result update(@RequestBody Category category){

        categoryService.updateById(category);

        return Result.success("菜品信息修改成功");
    }
    /*
    *
    *   根据条件查询分类数据
    *
    * */
    @GetMapping("/list")
    public Result list(Category category){
//        条件构造器
        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
//       添加条件
        categoryQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
//      添加排序时间
        categoryQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(categoryQueryWrapper);
        return Result.success(list);
    }
}
