package com.hoesl.example.chatapp.socket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoesl.example.chatapp.api.WebSocketSessionManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DefaultWebSocketHandler extends TextWebSocketHandler {

	protected final WebSocketSessionManager sessionManager;

    public DefaultWebSocketHandler(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public abstract void afterConnectionEstablished(WebSocketSession session) throws Exception;

    // Abstract method to force child classes to implement message handling
    @Override
    protected abstract void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception;

    @Override
    public abstract void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception;

}
    

