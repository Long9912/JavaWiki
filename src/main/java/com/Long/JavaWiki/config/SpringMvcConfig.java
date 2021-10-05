package com.Long.JavaWiki.config;

import com.Long.JavaWiki.interceptor.ActionInterceptor;
import com.Long.JavaWiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 配置未登录拦截请求和将静态文件请求映射到自定义路径
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Value("${file.localUrl}")
    private String fileUrl;

    @Resource
    LoginInterceptor loginInterceptor;

    @Resource
    ActionInterceptor actionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/ebook/all",
                        "/doc/all/**",
                        "/doc/vote/**",
                        "/doc/findContent/**",
                        "/image/**",
                        "/ebookSnapshot/**",
                        "/error",

                        //不拦截swagger3.0相关文件
                        "/swagger**/**",
                        "/webjars/**",
                        "/v3/**",
                        "/doc.html"
                        );
        //权限拦截
        registry.addInterceptor(actionInterceptor)
                .addPathPatterns(
                        "/*/save",
                        "/*/delete/**",
                        "/*/resetPassword",
                        "/file/*");
    }

    /**
     * 配置静态资源映射路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry register){
        register.addResourceHandler("/image/**").addResourceLocations("file:"+fileUrl);
        register.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

}
