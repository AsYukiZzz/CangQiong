package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/{clientId}")
public class ManagementSideWebSocket {

    private static final HashMap<String, Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        log.info("已建立与管理端ClientId={}的连接",clientId);
        sessionMap.put(clientId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId) throws IOException {
        log.info("已断开与管理端ClientId={}的连接",clientId);
        sessionMap.remove(clientId).close();
    }

    @OnMessage
    public void onMessage(String message, @PathParam("clientId") String clientId) {
        System.out.println("收到来自管理端：" + clientId + "的信息:" + message);
    }

    public void sendMessage2AllClient(String message){
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
