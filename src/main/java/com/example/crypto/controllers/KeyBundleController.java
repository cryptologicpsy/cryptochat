package com.example.crypto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class KeyBundleController {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.api.secret}")
    private String apiSecret;

    public KeyBundleController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ------------------- Helper -------------------
    private boolean checkApiKey(String authHeader) {
        return authHeader != null && authHeader.equals("Bearer " + apiSecret);
    }

    // ------------------- Upload PreKeyBundle -------------------
    @PostMapping("/{userId}/upload")
    public ResponseEntity<Map<String, Object>> uploadPreKeyBundle(
            @PathVariable String userId,
            @RequestBody Map<String, Object> bundle,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        try {
            String key = "prekey:bundle:" + userId;
            redisTemplate.opsForValue().set(key, bundle, 30, TimeUnit.DAYS);

            return ResponseEntity.ok(Map.of(
                    "message", "PreKeyBundle uploaded successfully",
                    "userId", userId
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to upload PreKeyBundle: " + e.getMessage()
            ));
        }
    }

    // ------------------- Get PreKeyBundle -------------------
    @GetMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> getPreKeyBundle(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        try {
            String key = "prekey:bundle:" + userId;
            Object bundle = redisTemplate.opsForValue().get(key);

            if (bundle == null) {
                return ResponseEntity.status(404).body(Map.of("error", "PreKeyBundle not found"));
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> bundleMap = (Map<String, Object>) bundle;
            return ResponseEntity.ok(bundleMap);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to retrieve PreKeyBundle: " + e.getMessage()
            ));
        }
    }

    // ------------------- Delete PreKeyBundle -------------------
    @DeleteMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> deletePreKeyBundle(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        try {
            String key = "prekey:bundle:" + userId;
            redisTemplate.delete(key);

            return ResponseEntity.ok(Map.of("message", "PreKeyBundle deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to delete PreKeyBundle: " + e.getMessage()
            ));
        }
    }
}
