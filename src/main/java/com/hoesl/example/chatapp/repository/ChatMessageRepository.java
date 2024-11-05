package com.hoesl.example.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoesl.example.chatapp.entities.ChatMessage;
import com.hoesl.example.chatapp.enums.MessageStatus;


import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySession(String sessionId); 

    List<ChatMessage> findBySessionAndStatus(String sessionId, MessageStatus status);
}
