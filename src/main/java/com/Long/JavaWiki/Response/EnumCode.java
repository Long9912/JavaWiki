package com.Long.JavaWiki.Response;
public enum EnumCode {
    // 这里的代码相当于：public static  final DataEnumCode SUCCESS = new DataEnumCode(0,“ok”)调用类有参构造传值
    // 定义成功的枚举常量，状态码，和描述
    SUCCESS(2000,"success"),

    SYSTEM_ERROR(5001,"服务器系统异常，请稍后..."),

    PARAMETER_ERROR(5002,"参数异常，认证失败"),

    USER_HAS_ERROR(5003,"用户名已存在"),

    LOGIN_ERROR(5004,"用户名或密码错误");// 注意上面的是逗号分隔，这里结束是分号

    // 定义的枚举常量属性。
    private int code;       // 状态码
    private String message; // 描述

    EnumCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
