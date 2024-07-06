package com.example.socialmedia.dtos;


import java.time.LocalDateTime;

public class PostDto {

    private Long id;
    private Long userId;
    private String content;
    private String image;
    private LocalDateTime createdAt;

    public PostDto(Long id, Long userId, String content,String image, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
    }

    // getters and setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}