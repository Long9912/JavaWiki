package com.Long.JavaWiki.util;


import java.io.Serializable;

public class RequestContext implements Serializable {
    //线程本地变量,通过AOP获取到远程ip,然后在本线程中保存远程地址
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
