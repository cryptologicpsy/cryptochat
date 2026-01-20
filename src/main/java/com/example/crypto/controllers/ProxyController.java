package com.example.crypto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class ProxyController {

    private final KeyBundleController keyBundleController;
    private final UserController userController;

    @Value("${app.api.secret}")
    private String apiSecret;

    public ProxyController(KeyBundleController keyBundleController,
                           UserController userController) {
        this.keyBundleController = keyBundleController;
        this.userController = userController;
    }

    private boolean checkApiKey(String authHeader) {
        return authHeader != null && authHeader.equals("Bearer " + apiSecret);
    }

    // ------------------- Users -------------------
    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                          @RequestBody Map<String, String> body) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return userController.registerUser(body, authHeader); // καλεί απευθείας
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                     @PathVariable String userId) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return userController.getUser(userId, authHeader);
    }

    @PutMapping("/users/{userId}/online")
    public ResponseEntity<?> setOnlineStatus(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                             @PathVariable String userId,
                                             @RequestBody Map<String, Boolean> body) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return userController.setOnlineStatus(userId, body, authHeader);
    }

    // ------------------- KeyBundles -------------------
    @PostMapping("/keys/{userId}/upload")
    public ResponseEntity<?> uploadKey(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                       @PathVariable String userId,
                                       @RequestBody Map<String, Object> body) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return keyBundleController.uploadPreKeyBundle(userId, body, authHeader, null);
    }

    @GetMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> getKey(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                    @PathVariable String userId) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return keyBundleController.getPreKeyBundle(userId, authHeader, null);
    }

    @DeleteMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> deleteKey(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                       @PathVariable String userId) {
        if (!checkApiKey(authHeader)) return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        return keyBundleController.deletePreKeyBundle(userId, authHeader, null);
    }
}
