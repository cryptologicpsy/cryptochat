package com.example.crypto.models;

import java.io.Serializable;
import java.time.Instant;

public class EncryptedMessage implements Serializable {
    
    private String messageId;
    private String senderId;
    private String recipientId;
    private String type;  // Changed from enum to String
    private String ciphertext;  // Changed from byte[] to String (base64)
    private Integer messageType;  // For Signal Protocol type (3 = PreKey, 1 = Regular)
    private int senderDeviceId;
    private int recipientDeviceId;
    private Instant timestamp;
    private boolean delivered;
    private boolean read;
    
    // Constructors
    public EncryptedMessage() {}
    
    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    public String getRecipientId() {
        return recipientId;
    }
    
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getCiphertext() {
        return ciphertext;
    }
    
    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
    
    public Integer getMessageType() {
        return messageType;
    }
    
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    
    public int getSenderDeviceId() {
        return senderDeviceId;
    }
    
    public void setSenderDeviceId(int senderDeviceId) {
        this.senderDeviceId = senderDeviceId;
    }
    
    public int getRecipientDeviceId() {
        return recipientDeviceId;
    }
    
    public void setRecipientDeviceId(int recipientDeviceId) {
        this.recipientDeviceId = recipientDeviceId;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isDelivered() {
        return delivered;
    }
    
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
}