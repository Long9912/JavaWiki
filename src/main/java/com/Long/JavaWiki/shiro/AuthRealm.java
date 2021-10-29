package com.Long.JavaWiki.shiro;

import com.Long.JavaWiki.response.UserLoginResp;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 自定义realm 从Redis中获取数据
 */
@Slf4j
@Component
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 认证 判断token的有效性
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取前端Header传入的token
        String accessToken = (String) token.getPrincipal();
        log.info("登录校验开始,token:{}", accessToken);

        //根据accessToken,在Redis中查询用户信息
        Object object = stringRedisTemplate.opsForValue().get(accessToken);
        if (object == null) {
            log.warn( "token无效,请求被拦截" );
            return null;
        } else {
            log.info("已登录:{}", object);
            //将Redis中的JSON反序列化
            UserLoginResp user=(JSON.parseObject((String) object, UserLoginResp.class));
            //构建 AuthenticationInfo对象并返回
            return new SimpleAuthenticationInfo(user, accessToken, this.getName());
        }
    }

    /**
     * 授权 获取用户的角色和权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //从 PrincipalCollection 中来获取登录用户的信息
        UserLoginResp user = (UserLoginResp) principals.getPrimaryPrincipal();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取权限
        String admin = user.getIsAdmin();
        if ("true".equals(admin)) {
            simpleAuthorizationInfo.addRole("admin");
            log.info("授予权限:admin");
        }else {
            simpleAuthorizationInfo.addRole("user");
            log.info("授予权限:user");
        }
        return simpleAuthorizationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof UsernamePasswordToken;
    }
}
