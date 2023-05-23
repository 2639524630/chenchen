package com.you.chen.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
*
*   全局异常处理器
*
* */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalException {

    /*
    *
    *   异常处理方法  sql
    *
    * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
      log.info("该异常为：{}",ex.getMessage());

      if (ex.getMessage().contains("Duplicate entry")){
          String[] split=ex.getMessage().split(" ");
          String msg= split[2];
          return Result.error("账号"+msg+"已存在");
      }

      return Result.error("未知错误");
    }
    /*
    *
    *    异常处理方法，自定义
    *
    * */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex){
        log.info("该异常为：{}",ex.getMessage());

        return Result.error(ex.getMessage());
    }
}
