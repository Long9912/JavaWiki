package com.Long.JavaWiki.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //这个bean会自动注册使用了@ServerEndpoint注解声明的对象,没有的话会报404
        return new ServerEndpointExporter();
    }

}
