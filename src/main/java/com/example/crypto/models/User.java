package com.example.crypto.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("User")
public class User implements Serializable {
    
    @Id
    private String userId;
    
    @Indexed
    private String username;
    
    private byte[] identityKeyPublic;
    private byte[] identityKeyPrivate;
    
    private Integer registrationId;
    
    private Instant createdAt;
    private Instant lastSeenAt;
    
    private boolean online;
    
    // Constructors
    public User() {}
    
    private User(Builder builder) {
        this.userId = builder.userId;
        this.username = builder.username;
        this.identityKeyPublic = builder.identityKeyPublic;
        this.identityKeyPrivate = builder.identityKeyPrivate;
        this.registrationId = builder.registrationId;
        this.createdAt = builder.createdAt;
        this.lastSeenAt = builder.lastSeenAt;
        this.online = builder.online;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String userId;
        private String username;
        private byte[] identityKeyPublic;
        private byte[] identityKeyPrivate;
        private Integer registrationId;
        private Instant createdAt;
        private Instant lastSeenAt;
        private boolean online;
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder identityKeyPublic(byte[] identityKeyPublic) {
            this.identityKeyPublic = identityKeyPublic;
            return this;
        }
        
        public Builder identityKeyPrivate(byte[] identityKeyPrivate) {
            this.identityKeyPrivate = identityKeyPrivate;
            return this;
        }
        
        public Builder registrationId(Integer registrationId) {
            this.registrationId = registrationId;
            return this;
        }
        
        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder lastSeenAt(Instant lastSeenAt) {
            this.lastSeenAt = lastSeenAt;
            return this;
        }
        
        public Builder online(boolean online) {
            this.online = online;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public byte[] getIdentityKeyPublic() {
        return identityKeyPublic;
    }
    
    public void setIdentityKeyPublic(byte[] identityKeyPublic) {
        this.identityKeyPublic = identityKeyPublic;
    }
    
    public byte[] getIdentityKeyPrivate() {
        return identityKeyPrivate;
    }
    
    public void setIdentityKeyPrivate(byte[] identityKeyPrivate) {
        this.identityKeyPrivate = identityKeyPrivate;
    }
    
    public Integer getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getLastSeenAt() {
        return lastSeenAt;
    }
    
    public void setLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public void setOnline(boolean online) {
        this.online = online;
    }
}