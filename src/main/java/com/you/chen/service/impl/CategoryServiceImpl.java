package com.you.chen.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.common.CustomException;
import com.you.chen.mapper.CategoryMapper;
import com.you.chen.pojo.Category;
import com.you.chen.pojo.Dish;
import com.you.chen.pojo.Setmeal;
import com.you.chen.service.CategoryService;
import com.you.chen.service.DishService;
import com.you.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

   /*
   *
   *   根据id删除分类，删除之前需要进行判断
   *
   * */
    @Override
    public void remove(Long id) {
//        查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        添加查询条件，根据分类id查询
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);
        if (count>0){
//            已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
//        查询分类是否已经关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmeaQueryWrapper = new LambdaQueryWrapper<>();
        setmeaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmeaQueryWrapper);
        if (count>0){
//          已经关联了套餐，抛出一个业务异常
            throw new CustomException("当前分类下以关联了套餐，不能删除");
        }
        //        正常删除分类
        super.removeById(id);
    }
}
