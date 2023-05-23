package com.you.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.you.chen.common.Result;
import com.you.chen.pojo.AddressBook;
import com.you.chen.service.AddressBookService;
import com.you.chen.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequestMapping("/addressBook")
@RestController
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    /*
    *
    *  新增地址
    *
    * */
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook){
        log.info("新增的地址为：{}",addressBook.toString());
        log.info("当前用户的id为：{}",BaseContext.getCurrentId());
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /*
    *
    *   设置默认地址
    *
    * */
    @GetMapping("/default")
    public Result  defaultAdd(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook==null){
            return Result.error("没有找到该对象");
        }
        return Result.success(addressBook);
    }

    /*
    *    地址展示
    *
    * */
    @GetMapping("/list")
    public Result list(AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
//    条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
//    查询当前用户设置的地址
        queryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return Result.success(list);
    }
    /*
    *
    *   设置默认地址
    *
    * */
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
     log.info("地址信息为:{}",addressBook.toString());
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(wrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

}
