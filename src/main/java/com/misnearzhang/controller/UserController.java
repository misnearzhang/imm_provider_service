package com.misnearzhang.controller;

import com.misnearzhang.pojo.User;
import com.misnearzhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhanglong on 2017/7/24.
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("userInfo")
    public List<User> getUserInfo(){
        return userService.getUserInfo();
    }
}
