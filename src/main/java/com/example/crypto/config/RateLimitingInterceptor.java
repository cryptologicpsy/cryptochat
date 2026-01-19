package com.example.crypto.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitingInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = getClientIP(request);
        String endpoint = request.getRequestURI();
        
        // Different limits for different endpoints
        int limit = getLimit(endpoint);
        int windowSeconds = getWindow(endpoint);
        
        String key = "rate_limit:" + endpoint + ":" + ipAddress;
        
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        
        if (count >= limit) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests. Please slow down.\"}");
            return false;
        }
        
        // Increment counter
        if (count == 0) {
            redisTemplate.opsForValue().set(key, "1", windowSeconds, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().increment(key);
        }
        
        return true;
    }
    
    private int getLimit(String endpoint) {
        if (endpoint.contains("/register")) {
            return 5; // 5 registrations per window
        } else if (endpoint.contains("/upload")) {
            return 10; // 10 key uploads per window
        }
        return 100; // Default: 100 requests per window
    }
    
    private int getWindow(String endpoint) {
        if (endpoint.contains("/register")) {
            return 3600; // 1 hour
        } else if (endpoint.contains("/upload")) {
            return 300; // 5 minutes
        }
        return 60; // Default: 1 minute
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}