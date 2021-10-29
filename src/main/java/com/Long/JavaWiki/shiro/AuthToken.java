package com.Long.JavaWiki.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义AuthenticationToken类,
 * 只有一个参数token,shiro登录时使用
 */
public class AuthToken extends UsernamePasswordToken {

    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
