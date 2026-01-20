package com.example.crypto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class ProxyController {

    @Value("${app.api.secret}")
    private String apiSecret;

    private final UserController userController;
    private final KeyBundleController keyBundleController;

    public ProxyController(UserController userController,
                           KeyBundleController keyBundleController) {
        this.userController = userController;
        this.keyBundleController = keyBundleController;
    }

    // ------------------- Users -------------------

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body,
                                          HttpServletRequest request) {
        // Δημιουργούμε Authorization header εσωτερικά
        String authHeader = "Bearer " + apiSecret;
        return userController.register(body, authHeader, request);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId,
                                     HttpServletRequest request) {
        String authHeader = "Bearer " + apiSecret;
        return userController.getUser(userId, authHeader, request);
    }

    @PutMapping("/users/{userId}/online")
    public ResponseEntity<?> setOnlineStatus(@PathVariable String userId,
                                             @RequestBody Map<String, Boolean> body,
                                             HttpServletRequest request) {
        String authHeader = "Bearer " + apiSecret;
        return userController.setOnlineStatus(userId, body, authHeader, request);
    }

    // ------------------- KeyBundles -------------------

    @PostMapping("/keys/{userId}/upload")
    public ResponseEntity<?> uploadKey(@PathVariable String userId,
                                       @RequestBody Map<String, Object> body,
                                       HttpServletRequest request) {
        String authHeader = "Bearer " + apiSecret;
        return keyBundleController.uploadPreKeyBundle(userId, body, authHeader, request);
    }

    @GetMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> getKey(@PathVariable String userId,
                                    HttpServletRequest request) {
        String authHeader = "Bearer " + apiSecret;
        return keyBundleController.getPreKeyBundle(userId, authHeader, request);
    }

    @DeleteMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> deleteKey(@PathVariable String userId,
                                       HttpServletRequest request) {
        String authHeader = "Bearer " + apiSecret;
        return keyBundleController.deletePreKeyBundle(userId, authHeader, request);
    }
}
