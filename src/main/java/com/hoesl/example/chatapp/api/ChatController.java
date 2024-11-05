package com.hoesl.example.chatapp.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hoesl.example.chatapp.dto.ChatMessageDto;
import com.hoesl.example.chatapp.entities.ChatMessage;
import com.hoesl.example.chatapp.entities.ChatMessageFactory;
 

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatMessageService chatService;

    public ChatController(ChatMessageService chatService) {
        this.chatService = chatService;
    } 

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessageDto messageDto) {
        ChatMessage message = ChatMessageFactory.createMessage(
            messageDto.getSender(),
            messageDto.getRecipient(),
            messageDto.getContent(),
            messageDto.getSessionId()
        );
        return chatService.sendMessage(message);
    }
 
    @GetMapping("/messages/{sessionId}")
    public List<ChatMessage> getMessages(@PathVariable String sessionId) {
        return chatService.getMessagesBySessionId(sessionId);
    }
}
