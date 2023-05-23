package com.you.chen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@EnableTransactionManagement
@ServletComponentScan
@SpringBootApplication
public class ChenchenApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChenchenApplication.class, args);
    }

}
