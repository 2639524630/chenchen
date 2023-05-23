package com.you.chen.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.common.CustomException;
import com.you.chen.dto.SetmealDto;
import com.you.chen.mapper.SetmealMapper;
import com.you.chen.pojo.Setmeal;
import com.you.chen.pojo.SetmealDish;
import com.you.chen.service.SetmealDishService;
import com.you.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService {



    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithMealAndDish(SetmealDto setmealDish) {
//       添加数据到setmeal表中
        this.save(setmealDish);
//      添加数据到setmeal_dish表中
        List<SetmealDish> dishes = setmealDish.getSetmealDishes();
        dishes.stream().map((item)->{
          item.setSetmealId(setmealDish.getId());
          return item;
        }).collect(Collectors.toList());
       setmealDishService.saveBatch(dishes);
    }

    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {
//        查询套餐状态，判断是否删除
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);

        int count=this.count(wrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
//        删除套餐表中的信息
        this.removeByIds(ids);
//        删除套餐菜品表中的信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper);
    }
}
