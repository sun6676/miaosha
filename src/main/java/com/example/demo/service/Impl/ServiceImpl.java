package com.example.demo.service.Impl;

import com.example.demo.mapper.userMapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean save(User user) {
        return userMapper.insert(user)==1;
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
    @Override
    public User findUser(String name) {
        return userMapper.selectname(name);
    }

    @Override
    public User findEmail(String email){
        return userMapper.selectemail(email);
    }
}
