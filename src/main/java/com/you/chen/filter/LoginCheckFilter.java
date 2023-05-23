package com.you.chen.filter;

import com.alibaba.fastjson.JSON;
import com.you.chen.common.Result;
import com.you.chen.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
*
*   检查用户是否已经完成登录
*
* */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
//  路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
//      获取本次请求uri
        String requestURI = request.getRequestURI();
//        拦截到请求
        log.info("拦截到请求：{}",requestURI);
//       定义不需要处理的请求路径
        String[] urls=new  String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
//       判断本次请求需要处理
        boolean b = check(urls, requestURI);
//        不需要处理，则直接放行
        if (b){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return ;
        }
//       判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee")!= null){
            log.info("用户以登录,用户id: {}",request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrenId(empId);
            filterChain.doFilter(request,response);
            return ;
       }
        if (request.getSession().getAttribute("user")!= null){
            log.info("用户以登录,用户id: {}",request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrenId(userId);
            filterChain.doFilter(request,response);
            return ;
        }

//        如果未登录则返回登录结果，通过输出流方式向客户端响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }



    /*
    *    路径匹配，检查本次请求是否放行
    *
    * */
    public boolean check(String[] urls,String URI){
        for (String url : urls) {
            boolean b = PATH_MATCHER.match(url, URI);
            if (b){
                return true;
            }
        }
        return false;
    }
}
