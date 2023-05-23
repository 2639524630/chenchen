package com.you.chen.config;


import com.you.chen.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源映射
     * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加映射
        log.info("映射资源开始");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/static/front/");


    }

    /*
    *
    *   扩展消息转换器
    *
    * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
//      创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
//        设置对象转换器，底层使用Jackson将java转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        将上面的消息转换器对象追加到mvc框架的转换器容器集合中
        converters.add(0,messageConverter);


    }
}
