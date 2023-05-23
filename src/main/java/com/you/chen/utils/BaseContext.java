package com.you.chen.utils;

/*
*
*   基于threadLocal的封装工具类，用于保存和获取当前登录用户id
*
* */
public class BaseContext {
    private static ThreadLocal<Long>  threadLocal=new ThreadLocal<>();

    public static void setCurrenId(Long id){
        threadLocal.set(id);
    }
    public static  Long getCurrentId(){
        return threadLocal.get();
    }
}
