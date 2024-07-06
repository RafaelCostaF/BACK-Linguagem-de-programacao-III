package com.example.socialmedia.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private byte[] profileImage;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
