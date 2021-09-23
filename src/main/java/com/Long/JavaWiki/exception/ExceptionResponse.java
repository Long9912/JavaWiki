package com.Long.JavaWiki.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类，封装异常信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    // 给用户看的描述信息
    private String respMsg;
    // 实际错误异常信息
    private String message;
    // 实际错误异常名字
    private String exceptionName;
}
