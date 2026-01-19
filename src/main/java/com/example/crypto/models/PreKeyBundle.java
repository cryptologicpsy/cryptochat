package com.example.crypto.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("PreKeyBundle")
public class PreKeyBundle implements Serializable {
    
    @Id
    private String id;
    private String userId;
    private int registrationId;
    private int deviceId;
    private byte[] identityKey;
    private int signedPreKeyId;
    private byte[] signedPreKeyPublic;
    private byte[] signedPreKeySignature;
    private Integer preKeyId;
    private byte[] preKeyPublic;
    
    // Constructors
    public PreKeyBundle() {}
    
    private PreKeyBundle(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.registrationId = builder.registrationId;
        this.deviceId = builder.deviceId;
        this.identityKey = builder.identityKey;
        this.signedPreKeyId = builder.signedPreKeyId;
        this.signedPreKeyPublic = builder.signedPreKeyPublic;
        this.signedPreKeySignature = builder.signedPreKeySignature;
        this.preKeyId = builder.preKeyId;
        this.preKeyPublic = builder.preKeyPublic;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String userId;
        private int registrationId;
        private int deviceId;
        private byte[] identityKey;
        private int signedPreKeyId;
        private byte[] signedPreKeyPublic;
        private byte[] signedPreKeySignature;
        private Integer preKeyId;
        private byte[] preKeyPublic;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder registrationId(int registrationId) {
            this.registrationId = registrationId;
            return this;
        }
        
        public Builder deviceId(int deviceId) {
            this.deviceId = deviceId;
            return this;
        }
        
        public Builder identityKey(byte[] identityKey) {
            this.identityKey = identityKey;
            return this;
        }
        
        public Builder signedPreKeyId(int signedPreKeyId) {
            this.signedPreKeyId = signedPreKeyId;
            return this;
        }
        
        public Builder signedPreKeyPublic(byte[] signedPreKeyPublic) {
            this.signedPreKeyPublic = signedPreKeyPublic;
            return this;
        }
        
        public Builder signedPreKeySignature(byte[] signedPreKeySignature) {
            this.signedPreKeySignature = signedPreKeySignature;
            return this;
        }
        
        public Builder preKeyId(Integer preKeyId) {
            this.preKeyId = preKeyId;
            return this;
        }
        
        public Builder preKeyPublic(byte[] preKeyPublic) {
            this.preKeyPublic = preKeyPublic;
            return this;
        }
        
        public PreKeyBundle build() {
            return new PreKeyBundle(this);
        }
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }
    
    public int getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
    
    public byte[] getIdentityKey() {
        return identityKey;
    }
    
    public void setIdentityKey(byte[] identityKey) {
        this.identityKey = identityKey;
    }
    
    public int getSignedPreKeyId() {
        return signedPreKeyId;
    }
    
    public void setSignedPreKeyId(int signedPreKeyId) {
        this.signedPreKeyId = signedPreKeyId;
    }
    
    public byte[] getSignedPreKeyPublic() {
        return signedPreKeyPublic;
    }
    
    public void setSignedPreKeyPublic(byte[] signedPreKeyPublic) {
        this.signedPreKeyPublic = signedPreKeyPublic;
    }
    
    public byte[] getSignedPreKeySignature() {
        return signedPreKeySignature;
    }
    
    public void setSignedPreKeySignature(byte[] signedPreKeySignature) {
        this.signedPreKeySignature = signedPreKeySignature;
    }
    
    public Integer getPreKeyId() {
        return preKeyId;
    }
    
    public void setPreKeyId(Integer preKeyId) {
        this.preKeyId = preKeyId;
    }
    
    public byte[] getPreKeyPublic() {
        return preKeyPublic;
    }
    
    public void setPreKeyPublic(byte[] preKeyPublic) {
        this.preKeyPublic = preKeyPublic;
    }
}