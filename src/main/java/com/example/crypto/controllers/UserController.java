package com.example.crypto.controllers;

import com.example.crypto.models.User;
import com.example.crypto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class UserController {

    private final UserRepository userRepository;

    @Value("${app.api.secret}")
    private String apiSecret;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean checkApiKey(String authHeader) {
        return authHeader != null && authHeader.equals("Bearer " + apiSecret);
    }

    // ------------------- Register User -------------------
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody Map<String, String> requestBody,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) 
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));

        String username = requestBody.get("username");
        if (username == null || username.trim().isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));

        username = username.trim();
        if (username.length() < 3 || username.length() > 20 || !username.matches("^[a-zA-Z0-9_]+$"))
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid username"));

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .registrationId(generateRegistrationId())
                .createdAt(Instant.now())
                .lastSeenAt(Instant.now())
                .online(true)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "registrationId", user.getRegistrationId(),
                "message", "User registered successfully"
        ));
    }

    // ------------------- Get User -------------------
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) 
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) 
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "registrationId", user.getRegistrationId(),
                "online", user.isOnline()
        ));
    }

    // ------------------- Set Online Status -------------------
    @PutMapping("/{userId}/online")
    public ResponseEntity<Map<String, Object>> setOnlineStatus(
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> body,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) 
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) 
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        User user = userOpt.get();
        user.setOnline(body.getOrDefault("online", false));
        user.setLastSeenAt(Instant.now());
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "online", user.isOnline()
        ));
    }

    // ------------------- Helper -------------------
    private int generateRegistrationId() {
        return new Random().nextInt(16380) + 1;
    }
}
