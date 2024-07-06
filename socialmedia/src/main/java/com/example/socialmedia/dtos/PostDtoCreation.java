package com.example.socialmedia.dtos;
import java.util.Objects;

public class PostDtoCreation {
    private String content;


    public PostDtoCreation(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
