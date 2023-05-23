package com.you.chen.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.mapper.UserMapper;
import com.you.chen.pojo.User;
import com.you.chen.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}
