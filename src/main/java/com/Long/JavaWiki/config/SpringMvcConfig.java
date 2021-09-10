package com.Long.JavaWiki.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//    @Resource
//    LogInterceptor logInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/user/login"
//                );
    }
}
