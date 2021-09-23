package com.Long.JavaWiki.util;


import java.io.Serializable;

public class RequestContext implements Serializable {
    /**
     * 线程本地变量，每个线程拥有本地变量的副本，各个线程之间的变量相互独立。
     * 在高并发场景下，可以实现无状态的调用，特别适用于各个线程依赖不同的变量值完成操作的场景
     * 通过AOP获取到远程ip,然后在本线程中保存远程地址
     */
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

    public static void removeRemoteAddr() {
        remoteAddr.remove();
    }

}
