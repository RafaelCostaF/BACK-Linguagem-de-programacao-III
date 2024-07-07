package com.example.socialmedia.controller;

import com.example.socialmedia.dtos.PostDto;
import com.example.socialmedia.dtos.PostDtoCreation;
import com.example.socialmedia.model.Posts;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.PostService;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

@RestController
@Tag(name = "Post Management", description = "Operations pertaining to post management")
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    @Operation(summary = "Get all posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Posts> posts = postService.findAll();
        List<PostDto> postDtos = posts.stream().map(post -> {
            String encodedImage = post.getImage() != null ? Base64.getEncoder().encodeToString(post.getImage()) : null;
            return new PostDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getContent(),
                    encodedImage,
                    post.getCreatedAt()
            );
        }).collect(Collectors.toList());


        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/me")
    @Operation(summary = "Get all posts for the authenticated user")
    public ResponseEntity<List<PostDto>> getMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Posts> posts = postService.findByUser(currentUser);
        List<PostDto> postDtos = posts.stream().map(post -> {
            String encodedImage = post.getImage() != null ? Base64.getEncoder().encodeToString(post.getImage()) : null;
            return new PostDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getContent(),
                    encodedImage,
                    post.getCreatedAt()
            );
        }).collect(Collectors.toList());


        return ResponseEntity.ok(postDtos);
    }
    
    @PostMapping
    @Operation(summary = "Create a new post.", description = "Send image as base64.")
    public ResponseEntity<PostDto> createPost(PostDtoCreation postDtoCreation) throws java.io.IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        byte[] image = Base64.getDecoder().decode(postDtoCreation.getImage());

        // Check if the uploaded file is a valid image
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
        if (bufferedImage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Returning ResponseEntity<String> for error case
        }
        
        Posts post = new Posts();
        post.setContent(postDtoCreation.getContent());
        post.setUser(currentUser);
        post.setImage(image);
        // createdAt will be set automatically by the @PrePersist method
        
        Posts savedPost = postService.save(post);
        
        PostDto postDto;
        if (savedPost.getImage() != null) {
            postDto = new PostDto(
                savedPost.getId(),
                savedPost.getUser().getId(),
                savedPost.getContent(),
                Base64.getEncoder().encodeToString(savedPost.getImage()),
                savedPost.getCreatedAt()
            );
        } else {
            postDto = new PostDto(
                savedPost.getId(),
                savedPost.getUser().getId(),
                savedPost.getContent(),
                null,  // or any default value you prefer for image when it's null
                savedPost.getCreatedAt()
            );
        }
            
            return ResponseEntity.ok(postDto);
        }
        

    @GetMapping("/{id}")
    @Operation(summary = "Get a post by id")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Optional<Posts> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Posts post = postOptional.get();
            String encodedImage = post.getImage() != null ? Base64.getEncoder().encodeToString(post.getImage()) : null;
            PostDto postDto = new PostDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getContent(),
                    encodedImage,
                    post.getCreatedAt()
            );
            return ResponseEntity.ok(postDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody Map<String, Object> postRequest) throws java.io.IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Posts> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Posts post = postOptional.get();
            if (!post.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update content if present in request
            if (postRequest.containsKey("content")) {
                post.setContent((String) postRequest.get("content"));
            }

            // Update image if present in request
            if (postRequest.containsKey("image")) {
                Object imageObj = postRequest.get("image");
                byte[] image = null;

                if (imageObj != null) {
                    if (imageObj instanceof String) {
                        String imageBase64 = (String) imageObj;
                        try {
                            image = Base64.getDecoder().decode(imageBase64);
                        } catch (IllegalArgumentException e) {
                            return ResponseEntity.badRequest().body(null); // Invalid Base64 string
                        }

                        // Check if the uploaded file is a valid image
                        BufferedImage bufferedImage;
                        try {
                            bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
                            if (bufferedImage == null) {
                                return ResponseEntity.badRequest().body(null); // Not a valid image
                            }
                        } catch (IOException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error reading image
                        }
                    } else {
                        return ResponseEntity.badRequest().body(null); // Invalid image format
                    }
                }

                // Update the image in the post entity
                post.setImage(image);
            }


            // Save the updated post
            Posts updatedPost = postService.save(post);

            // Prepare PostDto response
            String encodedImage = updatedPost.getImage() != null ? Base64.getEncoder().encodeToString(updatedPost.getImage()) : null;
            PostDto postDto = new PostDto(
                    updatedPost.getId(),
                    updatedPost.getUser().getId(),
                    updatedPost.getContent(),
                    encodedImage,
                    updatedPost.getCreatedAt()
            );

            return ResponseEntity.ok(postDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
    
        Optional<Posts> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Posts post = postOptional.get();
            if (!post.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            postService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
}
