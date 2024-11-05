package com.hoesl.example.chatapp.entities;

import com.hoesl.example.chatapp.enums.MessageStatus;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class ChatMessageFactory {

    public static ChatMessage createMessage(String sender, String recipient, String content, String sessionId) {
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSession(sessionId);
        
        message.setStatus(MessageStatus.PENDING);

        return message;
    }
}
