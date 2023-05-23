package com.you.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.you.chen.common.Result;
import com.you.chen.pojo.ShoppingCart;
import com.you.chen.service.ShoppingCartService;
import com.you.chen.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;



    /*
    *
    *    添加购物车
    *
    * */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart){
       log.info("购物车数据：{}",shoppingCart);
//       设置用户id，指定是哪个用户的购物车数据
        Long userid = BaseContext.getCurrentId();
        shoppingCart.setUserId(userid);
//        查询当前菜品或者套餐是否在购物车上
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userid);
        if (dishId!=null){
//            添加到购物车的是菜品
           queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
//        如果已经存在,就在原来数量基础上加1
        else{
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
//        如果不存在,则添加到购物车,默认就是1
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne!=null){
//           已经存在,更新操作
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }
        else{
//            不存在,新增
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }
        return Result.success(cartServiceOne);
    }

    @GetMapping("/list")
    public Result list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCart shoppingCart){
        log.info("收到的购物车信息:{}",shoppingCart);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if (shoppingCart.getDishId()!=null){
//            传进来的是菜品,查询菜品
         queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else {
//            传进来的是套餐
         queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
//        从数据库查询该菜品数据
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
//        判断数量是否为1,如果为1则删除,不为1,则数量减1
        if (cartServiceOne.getNumber()==1){
            shoppingCartService.removeById(cartServiceOne);
        }
        else{
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number-1);
            shoppingCartService.updateById(cartServiceOne);
        }
        return  Result.success(cartServiceOne);
    }
    @DeleteMapping("/clean")
    public Result clean(){
//        清空购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        查询当前用户下的购物车
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if (shoppingCarts==null){
            return Result.error("清空失败,您未添加商品到购物车");
        }
        else{
//            清空数据库中当前用户的购物车数据
            shoppingCartService.remove(queryWrapper);
        }
        return Result.success("清空成功");
    }
}
