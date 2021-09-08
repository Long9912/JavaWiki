package com.Long.JavaWiki.exception;

import com.Long.JavaWiki.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//全局异常处理器类
// 控制器类增强：可以对Controller中所有使用@RequestMapping注解的方法增强
@RestControllerAdvice(basePackages = "com.Long.JavaWiki.controller")
public class GlobalExceptionHandler {
    // 该注解是异常处理器注解，可以对指定异常类型处理，执行注解标注的方法（只要发生指定异常都会被拦截）
    @ExceptionHandler(Throwable.class)
    // 该注解用于指定异常处理方法执行后响应页面的HTTP状态码，HttpStatus是Spring内置的一个状态码枚举类，内定了详细的状态码及描述，当前获取的是500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)// 响应500
    public Object exceptionResponse(Exception ex, HttpServletRequest request) {
        ExceptionResponse resultError = new ExceptionResponse();
        if (ex instanceof NullPointerException) {               // 如果捕获的异常为空指针异常
            resultError.setRespMsg("服务器发生空指针异常，请稍后...");// 用户看到的异常信息
        } else if (ex instanceof ArithmeticException) {
            resultError.setRespMsg("服务器发生运算异常，请稍后...");
        } else {
            resultError.setRespMsg("服务器发生异常，请稍后...");
        }
        resultError.setTimestamp(new Date());                   // 设置异常发生时间
        resultError.setMessage(ex.getMessage());                // 实际发生的异常信息
        resultError.setExceptionName(ex.getClass().getName());  // 实际异常的名字
        resultError.setPath(request.getRequestURI());           // 异常RUI
        return ResponseData.error(EnumCode.SYSTEM_ERROR, resultError);
    }
}
