package com.you.chen.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.mapper.OrderDeatailMapper;
import com.you.chen.pojo.OrderDetail;
import com.you.chen.service.OrderDetailService;
import com.you.chen.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDeatailMapper, OrderDetail>
        implements OrderDetailService {


}
