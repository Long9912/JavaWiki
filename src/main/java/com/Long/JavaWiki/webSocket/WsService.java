package com.Long.JavaWiki.webSocket;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步任务类,向WebSocket推送消息
 */
@Service
public class WsService {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 推送消息,异步处理,这里的方法被自动注入使用ThreadPoolTaskExecutor作为TaskExecutor（线程池）
     */
    @Async
    public void sendInfo(String message,String logId) {
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }
}
