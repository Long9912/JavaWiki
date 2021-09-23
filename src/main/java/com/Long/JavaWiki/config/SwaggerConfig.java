package com.Long.JavaWiki.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi   // 开启Swagger自定义接口文档
@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true") //开发环境下配置为true
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * API基础信息定义
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JavaWiki")
                .description("Spring Boot + Vue3 前后端分离的wiki知识库系统")
                .contact(new Contact("Long12", "https://github.com/Long9912/JavaWiki", "792516830@qq.com"))
                .version("1.0")
                .build();
    }
}
