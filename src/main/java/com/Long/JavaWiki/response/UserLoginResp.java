package com.Long.JavaWiki.response;

import lombok.Data;

@Data
public class UserLoginResp {

    private Long id;

    private String loginName;

    private String name;

    private String isAdmin;

    private String token;

}
