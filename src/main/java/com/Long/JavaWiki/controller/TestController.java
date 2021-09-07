package com.Long.JavaWiki.controller;

import com.Long.JavaWiki.mapper.UserMapper;
import com.Long.JavaWiki.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户控制类")
@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @ApiOperation("查询全部的用户")
    @GetMapping("/hello")
    public List<User> hello(){
        return userMapper.selectList(null);
    }

    @ApiOperation("通过Id查询用户")
    @ApiImplicitParam(name = "id", value = "请传递一个ID",required = true, dataType = "String", paramType = "path")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id){
        Long value = Long.valueOf(id);
        return userMapper.selectById(value);
    }

    @PostMapping(value = "/user")
    @ApiOperation(value = "新增用户", notes = "测试RESTful之POST请求测试入参一个POJO（JSON格式）")
    public User postUser(@RequestBody User user) {
        return user;
    }
}
