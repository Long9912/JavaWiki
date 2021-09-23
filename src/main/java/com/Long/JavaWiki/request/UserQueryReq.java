package com.Long.JavaWiki.request;

import lombok.Data;

/**
 * 通过登录名查询用户
 */
@Data
public class UserQueryReq extends PageReq {

    private String loginName;

}
