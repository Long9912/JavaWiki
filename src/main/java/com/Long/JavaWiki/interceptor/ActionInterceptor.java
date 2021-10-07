package com.Long.JavaWiki.interceptor;

import com.Long.JavaWiki.exception.EnumCode;
import com.Long.JavaWiki.exception.ExceptionResponse;
import com.Long.JavaWiki.response.ResponseData;
import com.Long.JavaWiki.response.UserLoginResp;
import com.Long.JavaWiki.util.LoginUserContext;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
public class ActionInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ActionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }

        UserLoginResp userLoginResp = LoginUserContext.getUser();
        if ("true".equals(userLoginResp.getIsAdmin())) {
            //管理员用户不拦截
            return true;
        }

        LOG.info("操作被拦截");
        response.setStatus(HttpStatus.OK.value());
        ExceptionResponse resultError = new ExceptionResponse();
        resultError.setRespMsg("操作被拦截了,测试用户不开放增删改操作");
        ResponseData<ExceptionResponse> responseData = ResponseData.error(EnumCode.NOT_PERMISSIONS,resultError);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONObject.toJSON(responseData));
        //移除保存的用户数据
        LoginUserContext.removeUser();
        return false;
    }

}
