package com.hoesl.example.chatapp.socket.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoesl.example.chatapp.api.ChatConstants;
import com.hoesl.example.chatapp.api.ChatMessageService;
import com.hoesl.example.chatapp.api.WebSocketSessionManager;
import com.hoesl.example.chatapp.entities.ChatMessageFactory;

@Component
public class CustomerWebSocketHandler extends DefaultWebSocketHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	ChatMessageService chatMessageService;

	@Autowired
	ChatMessageFactory chatMessageFactory;

	public CustomerWebSocketHandler(WebSocketSessionManager sessionManager) {
		super(sessionManager);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionManager.addSession(ChatConstants.CUSTOMER, session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		JsonNode jsonNode = objectMapper.readTree(payload);

		String sender = jsonNode.get("sender").asText();
		String recipient = ChatConstants.OPERATOR;
		String content = jsonNode.get("content").asText();
		String sessionId = jsonNode.get("sessionId").asText();

		chatMessageService.sendMessage(ChatMessageFactory.createMessage(sender, recipient, content, sessionId));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionManager.removeSession(ChatConstants.CUSTOMER);
	}

}
