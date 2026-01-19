package com.example.crypto.controllers;

import com.example.crypto.models.User;
import com.example.crypto.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @PostMapping("/register")
public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
    String username = request.get("username");
    
    // Validation
    if (username == null || username.trim().isEmpty()) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Username is required");
        return ResponseEntity.badRequest().body(error);
    }
    
    username = username.trim();
    
    if (username.length() < 3) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Username must be at least 3 characters");
        return ResponseEntity.badRequest().body(error);
    }
    
    if (username.length() > 20) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Username must be at most 20 characters");
        return ResponseEntity.badRequest().body(error);
    }
    
    if (!username.matches("^[a-zA-Z0-9_]+$")) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Username can only contain letters, numbers, and underscores");
        return ResponseEntity.badRequest().body(error);
    }
    
    // Create user
    User user = User.builder()
            .userId(UUID.randomUUID().toString())
            .username(username)
            .registrationId(generateRegistrationId())
            .createdAt(Instant.now())
            .lastSeenAt(Instant.now())
            .online(true)
            .build();
    
    userRepository.save(user);
    
    Map<String, Object> response = new HashMap<>();
    response.put("userId", user.getUserId());
    response.put("username", user.getUsername());
    response.put("registrationId", user.getRegistrationId());
    response.put("message", "User registered successfully");
    
    return ResponseEntity.ok(response);
}

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable("userId") String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("registrationId", user.getRegistrationId());
        response.put("online", user.isOnline());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/online")
    public ResponseEntity<Map<String, Object>> setOnlineStatus(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, Boolean> request) {
        
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        user.setOnline(request.get("online"));
        user.setLastSeenAt(Instant.now());
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("online", user.isOnline());
        
        return ResponseEntity.ok(response);
    }
    
    // Generate random registration ID
    private Integer generateRegistrationId() {
        return new Random().nextInt(16380) + 1;
    }
}