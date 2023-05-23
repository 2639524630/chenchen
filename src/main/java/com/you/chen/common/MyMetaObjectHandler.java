package com.you.chen.common;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.you.chen.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
*
*  自定义元数据处理器
*
* */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /*
    *    插入操作自动填充
    *
    * */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert...]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }
    /*
    *
    *   更新操作自动填充
    *
    * */
    @Override
    public void updateFill(MetaObject metaObject) {
      log.info("公共字段自动填充[update]");

        long id=Thread.currentThread().getId();
        log.info("线程id为：{}",id);

        metaObject.setValue("updateTime", LocalDateTime.now());
      metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }


}
