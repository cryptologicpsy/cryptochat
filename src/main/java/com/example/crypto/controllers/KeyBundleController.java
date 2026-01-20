package com.example.crypto.controllers;

import com.example.crypto.models.PreKeyBundle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class KeyBundleController {

    private final RedisTemplate<String, Object> redisTemplate;

public KeyBundleController(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
}

    /**
     * Upload PreKeyBundle from frontend
     */
    @PostMapping("/{userId}/upload")
    public ResponseEntity<Map<String, Object>> uploadPreKeyBundle(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, Object> bundle) {
        
        try {
            // Store in Redis with 30 day expiration
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

    /**
     * Get PreKeyBundle for a user
     */
    @GetMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> getPreKeyBundle(
            @PathVariable("userId") String userId) {
        
        try {
            String key = "prekey:bundle:" + userId;
            Object bundle = redisTemplate.opsForValue().get(key);
            
            if (bundle == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "PreKeyBundle not found for user");
                return ResponseEntity.notFound().build();
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

    /**
     * Delete PreKeyBundle (for key rotation)
     */
    @DeleteMapping("/{userId}/bundle")
    public ResponseEntity<Map<String, Object>> deletePreKeyBundle(
            @PathVariable("userId") String userId) {
        
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
