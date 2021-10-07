package com.Long.JavaWiki.response;

import lombok.Data;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author Long9912
 * @since 2021-09-15
 */
@Data
public class UserQueryResp  {

    private Long id;

    private String loginName;

    private String name;

    private String password;

    private String isAdmin;

}
