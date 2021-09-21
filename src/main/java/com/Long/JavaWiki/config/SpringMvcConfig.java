package com.Long.JavaWiki.config;

import com.Long.JavaWiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Value("${file.localUrl}")
    private String fileUrl;

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/ebook/all",
                        "/doc/all/**",
                        "/doc/vote/**",
                        "/doc/findContent/**",
                        "/image/**",
                        "/error"
                        );
    }

    //配置图片映射路径
    public void addResourceHandlers(ResourceHandlerRegistry register){
        register.addResourceHandler("/image/**").addResourceLocations("file:"+fileUrl);
    }

}
