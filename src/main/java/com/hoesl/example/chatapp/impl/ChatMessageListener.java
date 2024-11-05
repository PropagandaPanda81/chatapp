package com.hoesl.example.chatapp.impl;

import com.hoesl.example.chatapp.api.ChatMessageService;
import com.hoesl.example.chatapp.config.RabbitMQConfig;
import com.hoesl.example.chatapp.entities.ChatMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
  
@Component
public class ChatMessageListener {

    private final ChatMessageService chatMessageService;

    public ChatMessageListener(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    public void receiveMessage(ChatMessage message) {
        chatMessageService.processMessage(message);
    }
}
