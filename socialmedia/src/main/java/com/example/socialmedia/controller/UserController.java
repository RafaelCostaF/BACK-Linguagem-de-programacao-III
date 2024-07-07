package com.example.socialmedia.controller;

import com.example.socialmedia.dtos.UserDto;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.UserService;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations pertaining to user management")
public class UserController {

    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        String encodedImage = currentUser.getImage() != null ? Base64.getEncoder().encodeToString(currentUser.getImage()) : null;
        
        UserDto userDto = new UserDto(
            currentUser.getId(),
            currentUser.getUsername(),
            currentUser.getFullName(),
            encodedImage
        );
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/me")
    @Operation(summary = "Update the authenticated user's details")
    public ResponseEntity<UserDto> updateAuthenticatedUser(@RequestBody Map<String, String> updates) throws IOException, java.io.IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (updates.containsKey("fullName")) {
            currentUser.setFullName((String) updates.get("fullName"));
        }
        if (updates.containsKey("username")) {
            currentUser.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("password")) {
            String encodedPassword = this.passwordEncoder.encode((String) updates.get("password"));
            currentUser.setPassword(encodedPassword);
        }
        if (updates.containsKey("image")) {
            String base64Image = (String) updates.get("image");
            byte[] image = Base64.getDecoder().decode(base64Image);

            if (image != null){
                // Check if the uploaded file is a valid image
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
                if (bufferedImage == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Returning ResponseEntity<String> for error case
                }
            } else {
                image = null;
            }
            currentUser.setImage(image);
        }

        userService.save(currentUser);

        String encodedImage = currentUser.getImage() != null ? Base64.getEncoder().encodeToString(currentUser.getImage()) : null;
        UserDto userDto = new UserDto(
            currentUser.getId(),
            currentUser.getUsername(),
            currentUser.getFullName(),
            encodedImage
        );

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> allUsers() {
        List <User> users = userService.allUsers();
        
        List<UserDto> usersDto = users.stream().map(user ->{ 
        String encodedImage = user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null;
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            encodedImage);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            String encodedImage = user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null;
            UserDto userDto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                encodedImage
            );
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete the authenticated user")
    public ResponseEntity<Void> deleteAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        userService.deleteById(currentUser.getId());  // Assuming you have a delete method in your userService to delete the user

        SecurityContextHolder.clearContext();  // Clear the security context after deleting the user

        return ResponseEntity.noContent().build();
    }


    // @PutMapping("/{id}")
    // @Operation(summary = "Update an existing post")
    // public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody Map<String, Object> postRequest) throws java.io.IOException {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     User currentUser = (User) authentication.getPrincipal();

    //     Optional<Posts> postOptional = postService.findById(id);
    //     if (postOptional.isPresent()) {
    //         Posts post = postOptional.get();
    //         if (!post.getUser().getId().equals(currentUser.getId())) {
    //             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    //         }

    //         // Update content if present in request
    //         if (postRequest.containsKey("content")) {
    //             post.setContent((String) postRequest.get("content"));
    //         }

    //         // Update image if present in request
    //         if (postRequest.containsKey("image")) {
    //             Object imageObj = postRequest.get("image");
    //             byte[] image = null;

    //             if (imageObj != null) {
    //                 if (imageObj instanceof String) {
    //                     String imageBase64 = (String) imageObj;
    //                     try {
    //                         image = Base64.getDecoder().decode(imageBase64);
    //                     } catch (IllegalArgumentException e) {
    //                         return ResponseEntity.badRequest().body(null); // Invalid Base64 string
    //                     }

    //                     // Check if the uploaded file is a valid image
    //                     BufferedImage bufferedImage;
    //                     try {
    //                         bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
    //                         if (bufferedImage == null) {
    //                             return ResponseEntity.badRequest().body(null); // Not a valid image
    //                         }
    //                     } catch (IOException e) {
    //                         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error reading image
    //                     }
    //                 } else {
    //                     return ResponseEntity.badRequest().body(null); // Invalid image format
    //                 }
    //             }

    //             // Update the image in the post entity
    //             post.setImage(image);
    //         }


    //         // Save the updated post
    //         Posts updatedPost = postService.save(post);

    //         // Prepare PostDto response
    //         String encodedImage = updatedPost.getImage() != null ? Base64.getEncoder().encodeToString(updatedPost.getImage()) : null;
    //         PostDto postDto = new PostDto(
    //                 updatedPost.getId(),
    //                 updatedPost.getUser().getId(),
    //                 updatedPost.getContent(),
    //                 encodedImage,
    //                 updatedPost.getCreatedAt()
    //         );

    //         return ResponseEntity.ok(postDto);
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }


}
