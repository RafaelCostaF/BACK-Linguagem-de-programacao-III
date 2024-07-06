package com.example.socialmedia.controller;

import com.example.socialmedia.dtos.PostDto;
import com.example.socialmedia.dtos.PostDtoCreation;
import com.example.socialmedia.model.Posts;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Posts> posts =  postService.findAll();
        List<PostDto> postDtos = posts.stream().map(post -> new PostDto(
            post.getId(),
            post.getUser().getId(),
            post.getContent(),
            post.getCreatedAt()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/me")
    @Operation(summary = "Get all posts for the authenticated user")
    public ResponseEntity<List<PostDto>> getMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Posts> posts = postService.findByUser(currentUser);
        List<PostDto> postDtos = posts.stream().map(post -> new PostDto(
            post.getId(),
            post.getUser().getId(),
            post.getContent(),
            post.getCreatedAt()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(postDtos);
    }
    
    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDtoCreation postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        String content = postRequest.getContent();
        
        Posts post = new Posts();
        post.setContent(content);
        post.setUser(currentUser);
        // createdAt will be set automatically by the @PrePersist method
        
        Posts savedPost = postService.save(post);
        
        PostDto postDto = new PostDto(
            savedPost.getId(),
            savedPost.getUser().getId(),
            savedPost.getContent(),
            savedPost.getCreatedAt()
            );
            
            return ResponseEntity.ok(postDto);
        }
        

    @GetMapping("/{id}")
    @Operation(summary = "Get a post by id")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Optional<Posts> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Posts post = postOptional.get();
            PostDto postDto = new PostDto(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getCreatedAt()
            );
            return ResponseEntity.ok(postDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody Map<String, String> postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Posts> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Posts post = postOptional.get();
            if (!post.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            post.setContent(postRequest.get("content"));

            Posts updatedPost = postService.save(post);

            PostDto postDto = new PostDto(
                updatedPost.getId(),
                updatedPost.getUser().getId(),
                updatedPost.getContent(),
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
