package com.beta.web.websocketController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/stockServer/{userId}/{stockId}")
@Component
public class StockServer {
    final Logger log = LoggerFactory.getLogger(StockServer.class);
    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, StockServer> webSocketMap = new ConcurrentHashMap<>();//维护用户id和server
    private static ConcurrentHashMap<String, String> userStockMap = new ConcurrentHashMap<>(); //维护用户id,股票id
    private Session session;
    private String userId = "";
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId,@PathParam("stockId") String stockId) {
        this.session = session;
        if(!webSocketMap.contains(userId)){
            addOnlineCount();
        }
        webSocketMap.put(userId,this);
        userStockMap.put(userId,stockId);
        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("有人发消息");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        onlineCount--;
    }
}
