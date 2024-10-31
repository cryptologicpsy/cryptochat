package com.example.crypto;

public class ChatMessage {
    private String content; // Content of the message

    // Default constructor (constructor without parameters)
    public ChatMessage() {}

    // Constructor with parameters for the content
    public ChatMessage(String content) {
        this.content = content;
    }

    // Getter and Setter for the 'content' field
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "content='" + content + '\'' +
                '}'; // String representation of the ChatMessage object
    }
}
