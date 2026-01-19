package com.example.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("SignalSession")
public class SignalSession implements Serializable {
    
    @Id
    private String id; // localUserId:remoteUserId:deviceId
    
    private String localUserId;
    private String remoteUserId;
    private int deviceId;
    
    // Serialized session state (Signal Protocol's SessionRecord)
    private byte[] sessionState;
    
    private Instant createdAt;
    private Instant updatedAt;
}