package com.hoesl.example.chatapp.api;

import java.util.List;

import com.hoesl.example.chatapp.entities.ChatMessage;

public interface ChatMessageService {
    ChatMessage sendMessage(ChatMessage message);
    List<ChatMessage> getMessages();
	List<ChatMessage> getMessagesBySessionId(String sessionId);
	void processMessage(ChatMessage message);
}