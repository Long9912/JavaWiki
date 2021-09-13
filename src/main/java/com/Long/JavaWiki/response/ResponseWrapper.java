package com.Long.JavaWiki.response;

import com.Long.JavaWiki.exception.EnumCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//对所有控制器中，被@RequestMapping注解标注的方法，进行增强
@RestControllerAdvice(basePackages = "com.Long.JavaWiki.controller")
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    //这个方法的返回值，决定是否启动结果响应拦截，当返回为true是，表示拦截
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 被拦截的响应，立即执行该方法。
     * @param responseObject ：是请求控制器方法接口后，响应的内容。
     */
    @Override
    public Object beforeBodyWrite(Object responseObject, MethodParameter returnType, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> Converter,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        //responseObject是否为null
        if (null == responseObject) {
            return ResponseData.error(EnumCode.DATA_EMPTY_ERROR,null);
        }
        if (returnType.getMethod().getReturnType().isAssignableFrom(Void.TYPE)) {
            return ResponseData.error(EnumCode.DATA_EMPTY_ERROR,null);
        }
        //responseObject是否是文件
        if (responseObject instanceof Resource) {
            return responseObject;
        }
        //如果已经封装成ResponseData,直接return
        if (responseObject instanceof ResponseData) {
            return responseObject;
        }
        //该方法返回的媒体类型是否是application/json。若不是，直接返回响应内容
        if (!mediaType.includes(MediaType.APPLICATION_JSON)) {
            return responseObject;
        }
        // String类型不能直接包装,用的是StringHttpMessageConverter转换器，无法转换为Json格式
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            // 将数据包装在ResponseData里后转换为json串进行返回
            try {
                return objectMapper.writeValueAsString(ResponseData.success(responseObject));
            } catch (JsonProcessingException e) {
                return ResponseData.error(EnumCode.SYSTEM_ERROR, "Json转换出错");
            }
        }
        return ResponseData.success(responseObject);
    }
}

