package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.UserLoginReq;
import com.Long.JavaWiki.request.UserQueryReq;
import com.Long.JavaWiki.request.UserResetReq;
import com.Long.JavaWiki.request.UserSaveReq;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.response.UserLoginResp;
import com.Long.JavaWiki.response.UserQueryResp;
import com.Long.JavaWiki.service.UserService;
import com.Long.JavaWiki.util.SnowFlake;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
@Api("用户控制类")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("分页查询用户")
    @ApiImplicitParam(name = "req", value = "传入分页参数,如果有传入登录名则模糊查询用户", required = true, dataType = "UserQueryReq", paramType = "query")
    @GetMapping("/list")
    public PageResp<UserQueryResp> list(@Validated UserQueryReq req) {
        PageResp<UserQueryResp> list = userService.getList(req);
        return list.getList().isEmpty() ? null : list;
    }

    @ApiOperation("编辑或新增用户")
    @PostMapping("/save")
    public String save(@Validated @RequestBody UserSaveReq req) {
        //将密码转为byte数组进行md5加密,然后转成16进制
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.saveOrUpdate(req);
        return "保存成功";
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public String resetPassword(@Validated @RequestBody UserResetReq req) {
        //将密码转为byte数组进行md5加密,然后转成16进制
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return "重置成功";
    }

    @ApiOperation("通过id逻辑删除用户")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.removeById(id);
        return "删除成功";
    }

    @ApiOperation("登录,登录成功后生成单点登录token")
    @PostMapping("/login")
    public UserLoginResp login(@Validated @RequestBody UserLoginReq req) {
        //将密码转为byte数组进行md5加密,然后转成16进制
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        log.info("生成单点登录token:{},存入Redis",token);
        userLoginResp.setToken(token.toString());

        //登录信息保存到redis,token作为key,登录信息作为value
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token.toString(), JSONObject.toJSONString(userLoginResp),1, TimeUnit.DAYS);

        return userLoginResp;
    }

    @ApiOperation("退出登录")
    @ApiImplicitParam(name = "token", value = "传入一个token", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/logout/{token}")
    public String logout(@PathVariable String token) {
        stringRedisTemplate.delete(token);
        log.info("从Redis中删除token:{}",token);
        return "退出成功";
    }

    @ApiOperation("设置管理员")
    @ApiImplicitParam(name = "id", value = "传入一个id", required = true, dataType = "String", paramType = "path")
    @PostMapping("/setAdmin/{id}")
    public String setAdmin(@PathVariable Long id) {
        userService.setAdmin(id);
        log.info("设置管理员:{}",id);
        return "设置成功";
    }

}

