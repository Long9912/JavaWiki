package com.Long.JavaWiki.util;


import com.Long.JavaWiki.response.UserLoginResp;

import java.io.Serializable;

public class LoginUserContext implements Serializable {

    private static ThreadLocal<UserLoginResp> user = new ThreadLocal<>();

    public static UserLoginResp getUser() {
        return user.get();
    }

    public static void setUser(UserLoginResp user) {
        LoginUserContext.user.set(user);
    }

    public static void removeUser() {
        user.remove();
    }

}
