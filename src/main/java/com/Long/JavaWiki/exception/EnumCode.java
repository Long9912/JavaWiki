package com.Long.JavaWiki.exception;
public enum EnumCode {
    // 这里的代码相当于：public static  final DataEnumCode SUCCESS = new DataEnumCode(0,“ok”)调用类有参构造传值
    // 定义成功的枚举常量，状态码，和描述
    SUCCESS(2000,"success"),

    SYSTEM_ERROR(5001,"服务器系统异常，请稍后..."),

    PARAMETER_ERROR(5002,"参数异常，认证失败"),

    DATA_EMPTY(5003,"响应数据为空"),

    USER_EXIST(5004,"用户名已存在"),

    LOGIN_ERROR(5005,"用户名或密码错误"),

    VOTE_REPEAT(5006,"您已点赞过");// 注意上面的是逗号分隔，这里结束是分号

    // 定义的枚举常量属性。
    private int code;       // 状态码
    private String desc; // 描述

    EnumCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
