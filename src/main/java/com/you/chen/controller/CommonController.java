package com.you.chen.controller;


import com.you.chen.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/*
*
*   文件上传和下载
*
* */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${chenchen.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
//        file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
       log.info("上传文件：{}",file.toString());
//     原始文件名
        String originalFileName=file.getOriginalFilename();
//     使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
//       创建一个目录对象
        File dir=new File(basePath);
//        判断当前目录是否存在
        if (!dir.exists()){
//            当前目录不存在
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(fileName);
    }
    /*
    *
    *    文件下载
    *
    * */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
//      输入流，通过输入流读取文件内容
         FileInputStream fileInputStream =new FileInputStream(new File(basePath+name));
//      输出流 ，通过输出流将文件写回浏览器，在浏览器中展示图片
        ServletOutputStream fiileOutputStream=response.getOutputStream();

        int len=0;
            byte[] bytes = new byte[1024];
           while(( len = fileInputStream.read(bytes) )!=-1 ){
               fiileOutputStream.write(bytes,0,len);
               fiileOutputStream.flush();
           }
           fileInputStream.close();
           fiileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
