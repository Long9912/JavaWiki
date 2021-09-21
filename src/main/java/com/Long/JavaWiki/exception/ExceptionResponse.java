package com.Long.JavaWiki.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//自定义异常类，封装异常信息
public class ExceptionResponse {
    private String respMsg;      // 给用户看的描述信息
    private String message;      // 实际错误异常信息
    private String exceptionName;// 实际错误异常名字
}
