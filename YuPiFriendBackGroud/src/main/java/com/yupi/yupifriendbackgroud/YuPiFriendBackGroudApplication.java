package com.yupi.yupifriendbackgroud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yupi.yupifriendbackgroud.mapper")
public class YuPiFriendBackGroudApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuPiFriendBackGroudApplication.class, args);
    }

}
