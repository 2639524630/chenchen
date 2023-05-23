package com.you.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.you.chen.common.Result;
import com.you.chen.pojo.User;
import com.you.chen.service.UserService;
import com.you.chen.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    /*
   *
   *   发送手机验证码的短信
   *
   * */
   @PostMapping("/sendMsg")
   public Result<String> sendMsg(@RequestBody User user, HttpSession session){
//     获取手机号
      String phone = user.getPhone();
//      生成随机的验证码
    if (StringUtils.isNotEmpty(phone)){
       String code = ValidateCodeUtils.generateValidateCode(4).toString();
       log.info("code={}",code);
       //      调用阿里云的短信服务发送短信
//       SMSUtils.sendMessage("晨晨外卖","",phone,code);
        session.setAttribute(phone,code);
      return Result.success("手机验证码发送成功");
    }
      return Result.error("短信发送失败");
   }
   @PostMapping("/login")
    public Result login(@RequestBody Map map,HttpSession session){
    log.info("map信息为：{}",map);
//    获取手机号
       String phone = map.get("phone").toString();
       String code = map.get("code").toString();
//      从session获取保存的手机号
       Object codeInSession = session.getAttribute(phone);
       if (codeInSession!=null&& codeInSession.equals(code)){
//           比对成功，登录成功
//           判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
           LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(User::getPhone,phone);
           User user = userService.getOne(queryWrapper);
           if (user==null){
//               是用户，自动完成注册
             user=new User();
             user.setPhone(phone);
             user.setStatus(1);
             userService.save(user);
           }
           session.setAttribute("user",user.getId());
           return Result.success(user);
       }
       return Result.success("登录失败");
   }
}
