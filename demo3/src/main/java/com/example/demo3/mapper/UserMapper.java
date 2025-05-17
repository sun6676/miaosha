package com.example.demo3.mapper;

import com.example.demo3.pojo.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    User selectuser(String name);
    int delteuser(int id);
}
