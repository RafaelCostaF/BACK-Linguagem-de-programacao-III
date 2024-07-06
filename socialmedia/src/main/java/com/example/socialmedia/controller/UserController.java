package com.example.socialmedia.controller;

import com.example.socialmedia.model.User;
import com.example.socialmedia.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations pertaining to user management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    // @GetMapping
    // @Operation(summary = "Get all users")
    // public List<User> getAllUsers() {
    //     return userService.findAll();
    // }

    // @GetMapping("/{id}")
    // @Operation(summary = "Get a user by id")
    // public Optional<User> getUserById(@PathVariable Long id) {
    //     return userService.findById(id);
    // }

    // @PostMapping
    // @Operation(summary = "Create a new user")
    // public User createUser(@RequestBody User user) {
    //     return userService.save(user);
    // }

    // @PutMapping("/{id}")
    // @Operation(summary = "Update an existing user")
    // public User updateUser(@PathVariable Long id, @RequestBody User user) {
    //     user.setId(id);
    //     return userService.save(user);
    // }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete a user")
    // public void deleteUser(@PathVariable Long id) {
    //     userService.deleteById(id);
    // }
}
