package com.Long.JavaWiki.controller;


import com.Long.JavaWiki.request.UserQueryReq;
import com.Long.JavaWiki.request.UserSaveReq;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.response.UserQueryResp;
import com.Long.JavaWiki.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
@Api("用户控制类")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

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
        userService.saveOrUpdate(req);
        return "保存成功";
    }

    @ApiOperation("通过id逻辑删除用户")
    @ApiImplicitParam(name = "id", value = "传入一个ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.removeById(id);
        return "删除成功";
    }
}

