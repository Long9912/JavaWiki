package com.Long.JavaWiki.controller;

import com.Long.JavaWiki.mapper.UserMapper;
import com.Long.JavaWiki.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/hello")
    public List<User> hello(){
        List<User> userList = userMapper.selectList(null);
        return userList;
    }
}
