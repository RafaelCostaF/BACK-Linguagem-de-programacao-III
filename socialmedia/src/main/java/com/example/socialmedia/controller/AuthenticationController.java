package com.example.socialmedia.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmedia.dtos.LoginUserDto;
import com.example.socialmedia.dtos.RegisterUserDto;
import com.example.socialmedia.dtos.UserDto;
import com.example.socialmedia.model.User;
import com.example.socialmedia.responses.LoginResponse;
import com.example.socialmedia.service.AuthenticationService;
import com.example.socialmedia.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody RegisterUserDto registerUserDto) throws IOException {
        User registeredUser = authenticationService.signup(registerUserDto);

        String encodedImage = registeredUser.getImage() != null ? Base64.getEncoder().encodeToString(registeredUser.getImage()) : null;
        UserDto registeredUserDto = new UserDto(
            registeredUser.getId(),
            registeredUser.getUsername(),
            registeredUser.getFullName(),
            encodedImage
        );

        return ResponseEntity.ok(registeredUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}