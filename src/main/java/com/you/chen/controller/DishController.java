package com.you.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.you.chen.common.Result;
import com.you.chen.dto.DishDto;
import com.you.chen.pojo.Category;
import com.you.chen.pojo.Dish;
import com.you.chen.pojo.DishFlavor;
import com.you.chen.service.CategoryService;
import com.you.chen.service.DishFlavorService;
import com.you.chen.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
*
*   菜品管理
*
* */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;
    /*
    *
    *   新增菜品
    *
    * */
    @PostMapping
    public Result save(@RequestBody DishDto dishDto){

    log.info("新增的菜品信息为：{}",dishDto.toString());

    dishService.saveWithFlaavor(dishDto);
    return Result.success("添加菜品成功");
    }
   /*
   *
   *   菜品信息分页查询
   *
   * */
   @GetMapping("/page")
   public Result page(int page,int pageSize,String name){

//    构建分页构造器对象
       Page<Dish> pageInfo = new Page<Dish>(page,pageSize);
       Page<DishDto> dishDtoPage = new Page<>();
//     条件构造器
       LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//      添加过滤条件
       queryWrapper.like(name!=null,Dish::getName,name);
//      添加排序条件
       queryWrapper.orderByDesc(Dish::getUpdateTime);
       dishService.page(pageInfo,queryWrapper);
//       进行对象的拷贝
       BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
       List<Dish> records = pageInfo.getRecords();
       List<DishDto> dishDtos = records.stream().map((item) -> {
           DishDto dishDto = new DishDto();
           BeanUtils.copyProperties(item, dishDto);
//          获取每个菜品的分类id
           Long categoryId = item.getCategoryId();
//           获得分类对象
           Category category = categoryService.getById(categoryId);
           String categoryName = category.getName();
           dishDto.setCategoryName(categoryName);

           return dishDto;
       }).collect(Collectors.toList());
       dishDtoPage.setRecords(dishDtos);
       return Result.success(dishDtoPage);
   }
   /*
   *
   *    根据id来查询菜品信息和对应的口味信息
   *
   * */
    @GetMapping("/{id}")
   public Result get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }
    @PutMapping
    public Result update(@RequestBody DishDto dishDto){
        dishService.updateWIthFlavour(dishDto);
        return Result.success("修改菜品成功");
    }
    /*
    *
    *   删除菜品
    *
    * */
    @DeleteMapping
    public Result delete(Long ids){
//      删除菜品何其对应的口味
        dishService.removeById(ids);
//        删除口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        return  Result.success("删除菜品成功");
    }
    /*
    *
    *   根据条件查询菜品数据
    *
    * */
 /*   @GetMapping("/list")
    public Result list(Dish dish){
//        创建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }*/
      @GetMapping("/list")
    public Result list(Dish dish){
//        创建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//      查询状态为1的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtos = list.stream().map((item) -> {
              DishDto dishDto = new DishDto();
              BeanUtils.copyProperties(item, dishDto);
//          获取每个菜品的分类id
//              Long categoryId = item.getCategoryId();


//           获得分类对象
//              Category category = categoryService.getById(categoryId);
//              String categoryName = category.getName();
//              dishDto.setCategoryName(categoryName);
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
          }).collect(Collectors.toList());

        return Result.success(dishDtos);
    }
}
