package com.example.crypto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = "https://chatapp.georgetzifkas.me")
public class ProxyController {

    @Value("${app.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ------------------- Users -------------------

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        return forwardPost("/api/users/register", body);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return forwardGet("/api/users/" + userId);
    }

    @PutMapping("/users/{userId}/online")
    public ResponseEntity<?> setOnlineStatus(
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> body
    ) {
        return forwardPut("/api/users/" + userId + "/online", body);
    }

    // ------------------- KeyBundles -------------------

    @PostMapping("/keys/{userId}/upload")
    public ResponseEntity<?> uploadKey(@PathVariable String userId, @RequestBody Map<String, Object> body) {
        return forwardPost("/api/keys/" + userId + "/upload", body);
    }

    @GetMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> getKey(@PathVariable String userId) {
        return forwardGet("/api/keys/" + userId + "/bundle");
    }

    @DeleteMapping("/keys/{userId}/bundle")
    public ResponseEntity<?> deleteKey(@PathVariable String userId) {
        return forwardDelete("/api/keys/" + userId + "/bundle");
    }

    // ------------------- Helper Methods -------------------

    private ResponseEntity<?> forwardPost(String path, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        // Χρησιμοποιούμε relative path για να μην έχουμε localhost
        return restTemplate.exchange(path, HttpMethod.POST, entity, Map.class);
    }

    private ResponseEntity<?> forwardPut(String path, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(path, HttpMethod.PUT, entity, Map.class);
    }

    private ResponseEntity<?> forwardGet(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiSecret);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(path, HttpMethod.GET, entity, Map.class);
    }

    private ResponseEntity<?> forwardDelete(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiSecret);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(path, HttpMethod.DELETE, entity, Map.class);
    }
}
