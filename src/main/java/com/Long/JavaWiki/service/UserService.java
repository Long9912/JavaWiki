package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.User;
import com.Long.JavaWiki.request.UserLoginReq;
import com.Long.JavaWiki.request.UserQueryReq;
import com.Long.JavaWiki.request.UserResetReq;
import com.Long.JavaWiki.request.UserSaveReq;
import com.Long.JavaWiki.response.PageResp;
import com.Long.JavaWiki.response.UserLoginResp;
import com.Long.JavaWiki.response.UserQueryResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
public interface UserService extends IService<User> {
    /**
     * 查询用户
     * @param req 用户名
     * @return  返回用户分页信息
     */
    PageResp<UserQueryResp> getList(UserQueryReq req);

    /**
     * 保存或更新用户
     */
    void saveOrUpdate(UserSaveReq req);

    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     */
    User selectByLoginName(String loginName);

    /**
     * 重置密码,密码先在前端md5加密传输,再到后端md5加密保存
     */
    void resetPassword(UserResetReq req);

    /**
     * 用户登录,密码先在前端md5加密传输,再到后端md5加密后
     * 在数据库根据登录名查询用户,对比密码是否一致
     * @param req 用户名 md5加密的密码
     * @return id 用户名 昵称
     */
    UserLoginResp login(UserLoginReq req);

    /**
     * 设置管理员
     * @param id 用户id
     */
    void setAdmin(Long id);
}
