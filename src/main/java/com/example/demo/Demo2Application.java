package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.example.demo.mapper.userMapper"})
@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo2Application.class, args);
//        Service service = run.getBean(Service.class);
//        User user = new User();
//        user.setName("张");
//        user.setPassword("123456");
//        user.setEmail("test@test.com");
//        service.save(user);
    }

}