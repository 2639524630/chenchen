package com.you.chen.controller;


import com.you.chen.common.Result;
import com.you.chen.pojo.Orders;
import com.you.chen.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("order")
@RestController
public class OrderController {

    @Autowired
    private OrdersService ordersService;
    /*
    *
    *   用户下单
    *
    * */
   @PostMapping("/submit")
    public Result subsit(@RequestBody Orders orders){
       ordersService.submit(orders);
       return Result.success("下单成功");
   }
}
