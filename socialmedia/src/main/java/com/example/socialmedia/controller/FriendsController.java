package com.example.socialmedia.controller;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmedia.dtos.UserDto;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.service.FriendsService;
import com.example.socialmedia.service.MessageService;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {

    @Autowired
    private FriendsService friendService;
    @Autowired
    private UserRepository userRepository;

    
    public FriendsController(FriendsService friendService, UserRepository userRepository) {
        this.friendService = friendService;
        this.userRepository = userRepository;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<String> addFriend(@PathVariable("id") Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String encodedImage = currentUser.getImage() != null ? Base64.getEncoder().encodeToString(currentUser.getImage()) : null;
        
        Long userId = currentUser.getId();

        friendService.addFriend(userId, friendId);

        return ResponseEntity.ok("Friend added successfully.");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeFriend(@PathVariable("id") Long friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        friendService.removeFriend(userId, friendId);

        return ResponseEntity.ok("Friend removed successfully.");
    }


    @GetMapping("/ifollow")
    public ResponseEntity<List<UserDto>> myFriends() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        List<User> friends = friendService.getMyFriends(userId);

        List<UserDto> friendsDto = friends.stream().map(user -> {
            String encodedImage = user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null;
            return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                encodedImage
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(friendsDto);
    }

    @GetMapping("/followme")
    public ResponseEntity<List<UserDto>> myFollowers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long userId = currentUser.getId();

        List<User> followers = friendService.getMyFollowers(userId);

        List<UserDto> followersDto = followers.stream().map(user -> {
            String encodedImage = user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null;
            return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                encodedImage
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(followersDto);
    }
}
