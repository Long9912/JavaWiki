package com.Long.JavaWiki.interceptor;

import com.Long.JavaWiki.response.UserLoginResp;
import com.Long.JavaWiki.util.LoginUserContext;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 * 作为简单的安全框架使用,项目中已被Shiro代替
 */
//@Component
@Deprecated
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        LOG.info("------------- LoginInterceptor 开始 -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }

        String path = request.getRequestURL().toString();
        LOG.info("接口登录拦截: path:{}", path);

        //获取header的token参数
        String token = request.getHeader("token");
        LOG.info("登录校验开始,token:{}", token);
        if (token == null || token.isEmpty()) {
            LOG.info( "token为空,请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object object = stringRedisTemplate.opsForValue().get(token);
        if (object == null) {
            LOG.warn( "token无效,请求被拦截" );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            LOG.info("已登录:{}", object);
            //登录后保存用户消息,用来检查管理员权限
            LoginUserContext.setUser(JSON.parseObject((String) object, UserLoginResp.class));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("------------- LoginInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }

}
