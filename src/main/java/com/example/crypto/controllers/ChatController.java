package com.example.crypto.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Map<String, Object> incomingMessage) {
        log.info("Relaying encrypted message from {} to {}", 
                incomingMessage.get("senderId"), 
                incomingMessage.get("recipientId"));
        
        // Create response message preserving all fields
        Map<String, Object> message = new HashMap<>(incomingMessage);
        
        // Add metadata
        message.put("messageId", UUID.randomUUID().toString());
        message.put("timestamp", Instant.now().toString());
        message.put("delivered", false);
        message.put("read", false);
        
        // Log for debugging
        log.info("Ciphertext present: {}", message.containsKey("ciphertext"));
        log.info("Ciphertext length: {}", 
                message.get("ciphertext") != null ? message.get("ciphertext").toString().length() : 0);
        
        // Forward to recipient (server can't read it!)
        messagingTemplate.convertAndSend(
            "/topic/user/" + message.get("recipientId"),
            message
        );
    }
}