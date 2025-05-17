package com.example.demo3.service;

import com.example.demo3.pojo.User;



public interface Service {
    User findUser(String name);
    boolean findUseremail(int id);
}

