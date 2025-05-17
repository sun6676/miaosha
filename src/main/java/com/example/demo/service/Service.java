package com.example.demo.service;

import com.example.demo.pojo.User;

import java.util.List;

public interface Service {
    boolean save(User user);

    List<User> findAll();
    User findUser(String name);
    User findEmail(String email);
}
