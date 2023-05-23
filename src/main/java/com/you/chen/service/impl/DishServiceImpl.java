package com.you.chen.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.dto.DishDto;
import com.you.chen.mapper.DishMapper;
import com.you.chen.pojo.Dish;
import com.you.chen.pojo.DishFlavor;
import com.you.chen.service.DishFlavorService;
import com.you.chen.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
  implements DishService {

    @Autowired
    private DishFlavorService dishFlavorServicer;

    /*
    *
    * 新增菜品，同时保存对应的口味数据
    *
    * */
    @Override
    @Transactional
    public void saveWithFlaavor(DishDto dishDto) {
//       保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();
//        保存菜品口味数据到菜品口味表

        List<DishFlavor> flavors = dishDto.getFlavors();
/*        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDtoId);
        }*/
//        使用stream流
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorServicer.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
//      查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
//       查询当前菜品对应的口味信息，从dish_flavour查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorServicer.list(queryWrapper);
//        进行对象的拷贝
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Override
    public void updateWIthFlavour(DishDto dishDto) {
//       更新dish表基本信息
       this.updateById(dishDto);
//       清理当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorServicer.remove(queryWrapper);
//       添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)-> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorServicer.saveBatch(flavors);
    }

}
