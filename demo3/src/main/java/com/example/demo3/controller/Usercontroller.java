package com.example.demo3.controller;


import com.example.demo3.pojo.User;
import com.example.demo3.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Usercontroller {
    @Autowired
    private Service service;

    @GetMapping("/ss")
    public User findUser(@RequestParam("ll") String name){
        return service.findUser(name);
    }
    @GetMapping("/aa")
    public boolean findUseremail(@RequestParam("ii") int id ){
        return service.findUseremail(id);
    }
}
