package com.hoesl.example.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.hoesl.example.chatapp.socket.handler.CustomerWebSocketHandler;
import com.hoesl.example.chatapp.socket.handler.OperatorWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CustomerWebSocketHandler customerWebSocketHandler;
    private final OperatorWebSocketHandler operatorWebSocketHandler;

    public WebSocketConfig(CustomerWebSocketHandler customerWebSocketHandler, OperatorWebSocketHandler operatorWebSocketHandler) {
        this.customerWebSocketHandler = customerWebSocketHandler;
        this.operatorWebSocketHandler = operatorWebSocketHandler; 
    } 

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customerWebSocketHandler, "/chat/customer").setAllowedOrigins("*");
        registry.addHandler(operatorWebSocketHandler, "/chat/operator").setAllowedOrigins("*");
    }
}
