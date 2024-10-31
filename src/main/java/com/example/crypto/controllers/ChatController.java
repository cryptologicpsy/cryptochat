package com.example.crypto.controllers;

import com.example.crypto.ChatMessage;
import com.example.crypto.services.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        logger.info("Received message from client: {}", chatMessage);
        return chatMessage; // Επιστρέφει το μήνυμα στον client
    }


    /*
     * Developed by: Cryptologic
     * Description: This controller handles chat messages for the CryptoChat application.
     */

}
