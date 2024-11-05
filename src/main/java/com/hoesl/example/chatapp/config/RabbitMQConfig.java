package com.hoesl.example.chatapp.config;

import java.util.Arrays;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String CHAT_QUEUE = "chatQueue";

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("rabbitmq");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(serializerMessageConverter());
		return template;

	}

	@Bean
	public SerializerMessageConverter serializerMessageConverter() {
		SerializerMessageConverter converter = new SerializerMessageConverter();

		// @formatter:off
		converter.setAllowedListPatterns(Arrays.asList(
				"com.hoesl.example.chatapp.entities.*",
				"com.hoesl.example.chatapp.enums.*", 
				"java.lang.*", 
				"java.time.*"));
		// @formatter:on
		return converter;
	}

	@Bean
	public Queue chatQueue() {
		return new Queue(CHAT_QUEUE, true);
	}
}
