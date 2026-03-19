package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//SpringBoot的启动类
@SpringBootApplication
@MapperScan("com.neuedu.mapper")
public class AppTest {
    public static void main(String[] args) {
        SpringApplication.run(AppTest.class, args);
        System.out.println("Spring Boot服务已启动。。。");
    }
}

