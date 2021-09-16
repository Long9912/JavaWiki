package com.Long.JavaWiki.service;

import com.Long.JavaWiki.entity.User;
import com.Long.JavaWiki.request.UserQueryReq;
import com.Long.JavaWiki.request.UserResetReq;
import com.Long.JavaWiki.request.UserSaveReq;
import com.Long.JavaWiki.response.PageResp;
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
    PageResp<UserQueryResp> getList(UserQueryReq req);

    void saveOrUpdate(UserSaveReq req);

    User selectByLoginName(String loginName);

    void resetPassword(UserResetReq req);
}
