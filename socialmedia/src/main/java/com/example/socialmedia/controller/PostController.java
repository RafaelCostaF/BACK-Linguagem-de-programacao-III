package com.example.socialmedia.controller;

import com.example.socialmedia.model.Post;
import com.example.socialmedia.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Post Management", description = "Operations pertaining to post management")
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Get all posts")
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a post by id")
    public Optional<Post> getPostById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public Post createPost(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        return postService.save(post);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public void deletePost(@PathVariable Long id) {
        postService.deleteById(id);
    }
}
