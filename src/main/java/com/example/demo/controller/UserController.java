package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.demo.pojo.User;
import com.example.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private Service service;

    @GetMapping("/sun")
    @ResponseBody
    public User sun(@RequestParam("la") String name) {
        User user = service.findUser(name);
        System.out.println("查询结果：" + user); // 检查是否为 null
        return user;
    }

    @GetMapping("/mo")
    @ResponseBody
    public List<User> getAllUsers() {
        return service.findAll();
    }
    @GetMapping("/userForm")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "userForm"; // 确保这个视图名称正确指向了您的HTML文件
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (service.save(user)) {
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/sun?la=sun";
        } else {
            return "errorPage";
        }
    }

}
