package com.example.demo.mapper.userMapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息
 */

@Mapper
public interface UserMapper {
    int insert(User user);
    List<User> findAll();
    User selectname(String name);
    User selectemail(String email);
}
