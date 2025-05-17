package com.example.demo3.service.Impl;


import com.example.demo3.mapper.UserMapper;
import com.example.demo3.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo3.service.Service;


@org.springframework.stereotype.Service
public class ServiceImpl implements Service{


    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUser(String name){
        return userMapper.selectuser(name);
    }

    @Override
    public boolean findUseremail(int id) {

        int result = userMapper.delteuser(id); // 调用删除方法
        return result > 0; // 判断是否删除成功
    }
}
