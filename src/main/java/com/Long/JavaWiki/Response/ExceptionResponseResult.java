package com.Long.JavaWiki.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//自定义异常类，封装异常信息
public class ExceptionResponseResult {
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;      // 时间
    private String respMsg;      // 给用户看的描述信息
    private String message;      // 实际错误异常信息
    private String exceptionName;// 实际错误异常名字
    private String path;         // URI
}
