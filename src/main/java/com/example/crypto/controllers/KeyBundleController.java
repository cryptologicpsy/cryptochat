package com.example.crypto.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/keys/proxy")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class KeyBundleProxyController {

    private final KeyBundleController keyBundleController;

    @Value("${app.api.secret}")
    private String apiSecret;

    public KeyBundleProxyController(KeyBundleController keyBundleController) {
        this.keyBundleController = keyBundleController;
    }

    // ------------------- Upload PreKeyBundle -------------------
    @PostMapping("/{userId}/upload")
    public ResponseEntity<?> uploadPreKeyBundle(
            @PathVariable String userId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {

        // Καλεί απευθείας τον KeyBundleController προσθέτοντας το secret token
        return keyBundleController.uploadPreKeyBundle(userId, body, "Bearer " + apiSecret, request);
    }

    // ------------------- Get PreKeyBundle -------------------
    @GetMapping("/{userId}/bundle")
    public ResponseEntity<?> getPreKeyBundle(
            @PathVariable String userId,
            HttpServletRequest request) {

        return keyBundleController.getPreKeyBundle(userId, "Bearer " + apiSecret, request);
    }

    // ------------------- Delete PreKeyBundle -------------------
    @DeleteMapping("/{userId}/bundle")
    public ResponseEntity<?> deletePreKeyBundle(
            @PathVariable String userId,
            HttpServletRequest request) {

        return keyBundleController.deletePreKeyBundle(userId, "Bearer " + apiSecret, request);
    }
}
