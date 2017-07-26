package com.misnearzhang;

import com.misnearzhang.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglong on 2017/7/14.
 */
@RestController
@SpringBootApplication
public class AppIndex {
    public static void main(String[] args) {
        SpringApplication.run(AppIndex.class,args);
    }
}
