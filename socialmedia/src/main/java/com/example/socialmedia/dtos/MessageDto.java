package com.example.socialmedia.dtos;

import java.time.LocalDateTime;

public class MessageDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String image;
    private LocalDateTime sentAt;

    public MessageDto(Long id, Long senderId, Long receiverId, String content, String image, LocalDateTime sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.image = image;
        this.sentAt = sentAt;
    }

    // Getters and Setters

    public MessageDto() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getSentAt() {
        return this.sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    
}
