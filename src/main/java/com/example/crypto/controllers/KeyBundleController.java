package com.example.crypto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class KeyBundleController {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.api.secret}")
    private String apiSecret;

    // Rate limiting per IP
    private final Map<String, Long> lastRequestTime = new ConcurrentHashMap<>();
    private final long RATE_LIMIT_MS = 500; // 0.5 sec

    public KeyBundleController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ------------------- Helper Methods -------------------
    private boolean checkApiKey(String authHeader) {
        return authHeader != null && authHeader.equals("Bearer " + apiSecret);
    }

    private boolean checkRateLimit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Long last = lastRequestTime.get(ip);

        if (last != null && (now - last) < RATE_LIMIT_MS) {
            return false;
        }

        lastRequestTime.put(ip, now);
        return true;
    }

    // ------------------- Upload PreKeyBundle -------------------
    @PostMapping("/{userId}/upload")
    public ResponseEntity<Map<String, Object>> uploadPreKeyBundle(
            @PathVariable String userId,
            @RequestBody Map<String, Object> bundle,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Forbidden");
            return ResponseEntity.status(403).body(error);
        }

        if (!checkRateLimit(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Too many requests");
            return ResponseEntity.status(429).body(error);
        }

        try {
            String key = "prekey:bundle:" + userId;
            redisTemplate.opsForValue().set(key, bundle, 30, TimeUnit.DAYS);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "PreKeyBundle uploaded successfully");
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to upload PreKeyBundle: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ------------------- Get PreKeyBundle -------------------
    @GetMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> getPreKeyBundle(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Forbidden");
            return ResponseEntity.status(403).body(error);
        }

        if (!checkRateLimit(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Too many requests");
            return ResponseEntity.status(429).body(error);
        }

        try {
            String key = "prekey:bundle:" + userId;
            Object bundle = redisTemplate.opsForValue().get(key);

            if (bundle == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "PreKeyBundle not found");
                return ResponseEntity.status(404).body(error);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> bundleMap = (Map<String, Object>) bundle;
            return ResponseEntity.ok(bundleMap);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to retrieve PreKeyBundle: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ------------------- Delete PreKeyBundle -------------------
    @DeleteMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> deletePreKeyBundle(
            @PathVariable String userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        if (!checkApiKey(authHeader)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Forbidden");
            return ResponseEntity.status(403).body(error);
        }

        if (!checkRateLimit(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Too many requests");
            return ResponseEntity.status(429).body(error);
        }

        try {
            String key = "prekey:bundle:" + userId;
            redisTemplate.delete(key);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "PreKeyBundle deleted successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to delete PreKeyBundle: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
