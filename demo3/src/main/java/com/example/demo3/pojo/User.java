package com.example.demo3.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private String email;

//    public User(Long id, String name, String password, String email) {
//        this.id = id;
//        this.name = name;
//        this.password = password;
//        this.email = email;
//    }
}
