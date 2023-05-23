package com.you.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.you.chen.common.Result;
import com.you.chen.pojo.Employee;
import com.you.chen.service.EmployeeService;
import com.you.chen.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
    * 员工登录
    *
    * */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//       3.如果没有查询到则返回失败结果
        if (emp==null){
         return Result.error("账号不存在");
        }
//       4.进行比对
        if (!emp.getPassword().equals(password)){
            return Result.error("账号密码错误");
        }
//       5.查看员工状态,0表示禁用，1表示可用
        if (emp.getStatus()==0){
            return Result.error("当前账号正在封禁");
        }
//        登录成功，将员工id放入session并返回登录结果
        log.info("登录成功，将账户存入session中");
        request.getSession().setAttribute("employee",emp.getId());
        BaseContext.setCurrenId(emp.getId());
//       将用户id存入
        return Result.success(emp);
    }
    /*
    *   员工退出
    *
    * */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        try {
            request.getSession().removeAttribute("employee");
        } catch (Exception e) {
            return Result.error("退出失败");
        } finally {
            return Result.success("退出成功");
        }
    }
    /*
    *
    *  新增员工
    *
    * */
    @PostMapping
    public Result save(@RequestBody Employee employee,HttpServletRequest request){
         log.info("新增员工，员工信息：{}",employee.toString());
//         设置初始密码，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empid = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empid);
        employee.setUpdateUser(empid);*/
        employeeService.save(employee);
         return Result.success("添加员工成功");
    }
    /*
    *
    *  员工表分页查询
    *
    * */
    @GetMapping("/page")
    public Result page(int page,int pageSize,String name){
      log.info("page={},pagesize={},name={}",page,pageSize,name);
//       构造分页构造器
        Page pageInfo=new Page(page,pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
//        添加查询条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
//        添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }
    /*
    *   根据id来修改员工信息
    *
    * */
    @PutMapping
    public Result update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("修改的员工信息"+employee.toString());

        long id=Thread.currentThread().getId();
        log.info("线程id为{}",id);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)(request.getSession().getAttribute("employee")));
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }
    /*
    *
    *   根据id来查询员工信息
    * */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        log.info("根据id来查询员工信息。。。");
        Employee employee=employeeService.getById(id);
        if (employee!=null){

            return Result.success(employee);
        }
        return Result.error("没有查询到员工信息");
    }
}
