package com.you.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.you.chen.common.Result;
import com.you.chen.dto.SetmealDto;
import com.you.chen.pojo.Category;
import com.you.chen.pojo.Setmeal;
import com.you.chen.service.CategoryService;
import com.you.chen.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
*
*  套餐管理
*
* */
@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
/*
*     添加套餐
*
*
* */
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result save(@RequestBody SetmealDto setmealDto){
      setmealService.saveWithMealAndDish(setmealDto);
      return Result.success("添加套餐成功");
    }

    @GetMapping("/page")
    public  Result page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//      添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name!=null,Setmeal::getName,name);
//        添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);
//        进行copy除records
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtos);
        return Result.success(setmealDtoPage);
    }
    /*
    *
    *   删除套餐
    *
    * */
     @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){
       log.info("要删除的菜品的id值：{}",ids);
       setmealService.deleteWithDish(ids);
        return Result.success("套餐数据删除成功");
     }

     /*
     *
     *    根据条件查询套餐数据
     *
     * */
    @GetMapping("/list")
    public Result list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return Result.success(list);
    }
}
