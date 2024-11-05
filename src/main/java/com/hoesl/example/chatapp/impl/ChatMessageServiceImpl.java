package com.hoesl.example.chatapp.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hoesl.example.chatapp.api.ChatMessageService;
import com.hoesl.example.chatapp.api.WebSocketSessionManager;
import com.hoesl.example.chatapp.config.RabbitMQConfig;
import com.hoesl.example.chatapp.entities.ChatMessage;
import com.hoesl.example.chatapp.enums.MessageStatus;
import com.hoesl.example.chatapp.repository.ChatMessageRepository;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository messageRepository;
	private final RabbitTemplate rabbitTemplate;
	private final WebSocketSessionManager sessionManager;
	private final ObjectMapper objectMapper;

	public ChatMessageServiceImpl(ChatMessageRepository messageRepository, RabbitTemplate rabbitTemplate,
			WebSocketSessionManager sessionManager) {
		this.messageRepository = messageRepository;
		this.rabbitTemplate = rabbitTemplate;
		this.sessionManager = sessionManager;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());

	}

	@Override
	public ChatMessage sendMessage(ChatMessage message) {
		ChatMessage savedMessage = messageRepository.save(message);
		savedMessage.setStatus(MessageStatus.PENDING);

		String recipientId = message.getRecipient();
		String senderId = message.getSender();

		if (sessionManager.getSession(recipientId) != null) {
			String jsonMessage = formatMessageAsJson(message);
			sessionManager.sendMessageToUser(recipientId, new TextMessage(jsonMessage));
			savedMessage.setStatus(MessageStatus.SENT);
		} else {
			rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_QUEUE, savedMessage);
		}

		return savedMessage;
	}

	private String formatMessageAsJson(ChatMessage message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}"; //
		}
	}

	@Override
	public void processMessage(ChatMessage message) {
		message.setStatus(MessageStatus.SENT);
		messageRepository.save(message);

		sendToRecipient(message);
	}

	private void sendToRecipient(ChatMessage message) {
		String recipientId = message.getRecipient();

		if (sessionManager.getSession(recipientId) != null) {
			String jsonMessage = formatMessageAsJson(message);
			sessionManager.sendMessageToUser(recipientId, new TextMessage(jsonMessage));
		} else {
			rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_QUEUE, message);
		}
	}

	@Override
	public List<ChatMessage> getMessagesBySessionId(String sessionId) {
		return messageRepository.findBySession(sessionId);
	}

	@Override
	public List<ChatMessage> getMessages() {
		return messageRepository.findAll();
	}
}
