package com.example.socialmedia.dtos;

import java.util.Objects;

public class PostDtoCreation {
    private String content;
    private String image;

    public PostDtoCreation(String content, String image) {
        this.content = content;
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDtoCreation that = (PostDtoCreation) o;
        return Objects.equals(content, that.content) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, image);
    }
}
