package com.funtl.oauth2;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhan
 * @since 2019-05-30 17:08
 */
@SpringBootApplication
@MapperScan(basePackages = "com.funtl.oauth2.mapper")
public class Oauth2ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }
}
