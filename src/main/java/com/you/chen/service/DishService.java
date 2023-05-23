package com.you.chen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.you.chen.dto.DishDto;
import com.you.chen.pojo.Dish;

public interface DishService extends IService<Dish> {

//    新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish，dish_flavour
    public  void saveWithFlaavor(DishDto dishDto);

    public  DishDto getByIdWithFlavor(Long id);

    public void updateWIthFlavour(DishDto dishDto);
}
