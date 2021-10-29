package com.Long.JavaWiki.shiro;

import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.exception.ExceptionResponse;
import com.Long.JavaWiki.response.ResponseData;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shiro自定义auth过滤器
 */
@Slf4j
@Component
public class AuthFilter extends AuthenticatingFilter {
    /**
     * 获取前端Header传入的token,生成自定义token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        //向下转型成HttpServletRequest
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //获取header的token参数
        String token = httpServletRequest.getHeader("token");
        return new AuthToken(token);
    }

    /**
     * 前后端分离的架构,
     * 前端会发一个OPTIONS请求先做预检,
     * 对预检请求不做校验,其他请求全部拒绝访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    /**
     * 拒绝访问的请求，会调用onAccessDenied方法，onAccessDenied方法先获取 token，再调用executeLogin方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //向下转型成HttpServletRequest
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //获取header的token参数,如果token不存在,直接返回
        String token = httpRequest.getHeader("token");
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            log.info("token为空,请求被拦截:{}",httpRequest.getRequestURL());
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));

            ExceptionResponse resultError = new ExceptionResponse();
            resultError.setRespMsg("请登录系统");
            ResponseData<ExceptionResponse> responseData = ResponseData.error(EnumCode.NOT_PERMISSIONS, resultError);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().print(JSONObject.toJSON(responseData));
            return false;
        }
        return executeLogin(request, response);
    }

    /**
     * token失效时候调用
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));

            ExceptionResponse resultError = new ExceptionResponse();
            resultError.setRespMsg("登录凭证已失效，请重新登录");
            ResponseData<ExceptionResponse> responseData = ResponseData.error(EnumCode.NOT_PERMISSIONS, resultError);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().print(JSONObject.toJSON(responseData));
        } catch (IOException ex) {
            log.info("错误:{}",ex.getMessage());
        }
        return false;
    }
}
