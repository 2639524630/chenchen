package com.you.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.mapper.DishFlavorMapper;
import com.you.chen.pojo.DishFlavor;
import com.you.chen.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
        implements DishFlavorService {

}
