package com.you.chen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.you.chen.dto.SetmealDto;
import com.you.chen.pojo.Setmeal;
import com.you.chen.pojo.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /*
    *    新增套餐，同时保存到套餐和菜品表
    *
    * */
    public void saveWithMealAndDish(SetmealDto setmealDish);

   /*
   *     删除套餐，同时删除套餐和菜品表中的数据
   *
   * */
    public void deleteWithDish(List<Long> ids);
}
