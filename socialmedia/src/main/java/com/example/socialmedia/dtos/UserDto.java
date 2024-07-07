package com.example.socialmedia.dtos;

public class UserDto {
    
    private Long id;
    private String email;
    private String fullName;
    private String image;


    public UserDto(Long id, String email, String fullName, String image) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.image = image;
    }


    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
