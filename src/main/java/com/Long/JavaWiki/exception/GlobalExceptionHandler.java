package com.Long.JavaWiki.exception;

import com.Long.JavaWiki.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//全局异常处理器类
// 控制器类增强：可以对Controller中所有使用@RequestMapping注解的方法增强
@RestControllerAdvice(basePackages = "com.Long.JavaWiki.controller")
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 获取异常响应对象
     */
    private ExceptionResponse getResult(Exception ex, HttpServletRequest request, String message) {
        ExceptionResponse resultError = new ExceptionResponse();
        resultError.setRespMsg(message);                        // 用户看到的异常信息
        resultError.setTimestamp(new Date());                   // 设置异常发生时间
        resultError.setMessage(ex.getMessage());                // 实际发生的异常信息
        resultError.setPath(request.getRequestURI());           // 异常RUI
        resultError.setExceptionName(ex.getClass().getName());  // 实际异常的名字
        return resultError;
    }

    // 该注解是异常处理器注解，可以对指定异常类型处理，执行注解标注的方法（只要发生指定异常都会被拦截）
    @ExceptionHandler(RuntimeException.class)
    // 该注解用于指定异常处理方法执行后响应页面的HTTP状态码，HttpStatus是Spring内置的一个状态码枚举类，内定了详细的状态码及描述，当前获取的是500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)// 响应500
    public ResponseData exceptionResponse(Exception ex, HttpServletRequest request) {
        String message = null;
        if (ex instanceof NullPointerException) {      // 如果捕获的异常为空指针异常
            message = "服务器发生空指针异常，请稍后...";     // 用户看到的异常信息
        } else if (ex instanceof ArithmeticException) {
            message = "服务器发生运算异常，请稍后...";
        } else {
            message = "服务器发生异常，请稍后...";
        }
        ExceptionResponse resultError = getResult(ex, request, message);
        return ResponseData.error(EnumCode.SYSTEM_ERROR, resultError);
    }

    /**
     * 校验异常统一处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)// 响应500
    public ResponseData validExceptionHandler(BindException ex, HttpServletRequest request) {
        LOG.warn("参数校验失败：{}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        String message = (ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ExceptionResponse resultError = getResult(ex, request, message);
        return ResponseData.error(EnumCode.PARAMETER_ERROR, resultError);
    }
}
