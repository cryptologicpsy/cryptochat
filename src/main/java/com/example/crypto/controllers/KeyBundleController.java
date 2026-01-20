package com.example.crypto.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class KeyBundleController {

    private final RedisTemplate<String, Object> redisTemplate;

    // Rate limiting
    private final Map<String, Long> lastRequestTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_MS = 500;

    // Token TTL (seconds)
    private static final long TOKEN_TTL = 30;

    public KeyBundleController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ---------------- Helpers ----------------

    private boolean checkRateLimit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Long last = lastRequestTime.get(ip);
        if (last != null && now - last < RATE_LIMIT_MS) return false;
        lastRequestTime.put(ip, now);
        return true;
    }

    private String bundleKey(String userId) {
        return "prekey:bundle:" + userId;
    }

    private String tokenKey(String token) {
        return "prekey:token:" + token;
    }

    // ---------------- Upload (protected) ----------------
    @PostMapping("/{userId}/upload")
    public ResponseEntity<?> uploadBundle(
            @PathVariable String userId,
            @RequestBody Map<String, Object> bundle,
            HttpServletRequest request) {

        if (!checkRateLimit(request))
            return ResponseEntity.status(429).body(Map.of("error", "Too many requests"));

        redisTemplate.opsForValue().set(bundleKey(userId), bundle, 30, TimeUnit.DAYS);

        return ResponseEntity.ok(Map.of("message", "Uploaded"));
    }

    // ---------------- Generate TEMP token ----------------
    @GetMapping("/token/{userId}")
    public ResponseEntity<?> generateToken(
            @PathVariable String userId,
            HttpServletRequest request) {

        if (!checkRateLimit(request))
            return ResponseEntity.status(429).body(Map.of("error", "Too many requests"));

        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                tokenKey(token),
                userId,
                TOKEN_TTL,
                TimeUnit.SECONDS
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "expiresIn", TOKEN_TTL
        ));
    }

    // ---------------- Get Bundle (token protected) ----------------
    @GetMapping("/{userId}/bundle")
    public ResponseEntity<?> getBundle(
            @PathVariable String userId,
            @RequestHeader(value = "X-PreKey-Token", required = false) String token,
            HttpServletRequest request) {

        if (!checkRateLimit(request))
            return ResponseEntity.status(429).body(Map.of("error", "Too many requests"));

        if (token == null)
            return ResponseEntity.status(403).body(Map.of("error", "Missing token"));

        Object tokenUser = redisTemplate.opsForValue().get(tokenKey(token));
        if (tokenUser == null || !tokenUser.equals(userId))
            return ResponseEntity.status(403).body(Map.of("error", "Invalid token"));

        // one-time use
        redisTemplate.delete(tokenKey(token));

        Object bundle = redisTemplate.opsForValue().get(bundleKey(userId));
        if (bundle == null)
            return ResponseEntity.status(404).body(Map.of("error", "Not found"));

        return ResponseEntity.ok(bundle);
    }
}
