package com.Long.JavaWiki.exception;

import com.Long.JavaWiki.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器类
 * 控制器类增强：可以对Controller中所有使用@RequestMapping注解的方法增强
 */
@RestControllerAdvice(basePackages = "com.Long.JavaWiki.controller")
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 获取异常响应对象
     */
    private ExceptionResponse getResult(Exception ex, HttpServletRequest request, String message) {
        ExceptionResponse resultError = new ExceptionResponse();
        // 用户看到的异常信息
        resultError.setRespMsg(message);
        // 实际发生的异常信息
        resultError.setMessage(ex.getMessage());
        // 实际异常的名字
        resultError.setExceptionName(ex.getClass().getName());
        return resultError;
    }

    /**
     * 该注解是异常处理器注解，可以对指定异常类型处理，执行注解标注的方法（只要发生指定异常都会被拦截）
     */
    @ExceptionHandler(Exception.class)
    public ResponseData exceptionResponse(Exception ex, HttpServletRequest request) {
        String message;
        if (ex instanceof NullPointerException) {
            message = "服务器发生空指针异常，请稍后...";
        } else if (ex instanceof ArithmeticException) {
            message = "服务器发生运算异常，请稍后...";
        } else {
            message = "服务器发生异常，请稍后...";
        }
        LOG.warn("发生异常:", ex);
        ExceptionResponse resultError = getResult(ex, request, message);
        return ResponseData.error(EnumCode.SYSTEM_ERROR, resultError);
    }

    /**
     * 校验异常统一处理
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseData validExceptionHandler(BindException ex, HttpServletRequest request) {
        LOG.warn("参数校验失败：{}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        String message = (ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ExceptionResponse resultError = getResult(ex, request, message);
        return ResponseData.error(EnumCode.PARAMETER_ERROR, resultError);
    }

    /**
     * 业务异常统一处理
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseData businessExceptionHandler(BusinessException ex, HttpServletRequest request) {
        EnumCode code = ex.getCode();
        LOG.warn("发生业务异常：{}", code.getDesc());
        String message = (code.getDesc());
        ExceptionResponse resultError = getResult(ex, request, message);
        return ResponseData.error(code, resultError);
    }
}
