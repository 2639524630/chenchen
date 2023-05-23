package com.you.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.pojo.Employee;
import com.you.chen.service.EmployeeService;
import com.you.chen.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 86152
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-04-22 21:04:08
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




