package com.example.socialmedia.service;

import com.example.socialmedia.model.Posts;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Posts> findByUser(User user) {
        return postRepository.findByUser(user);
    }

    public List<Posts> findAll() {
        return postRepository.findAll();
    }

    public Optional<Posts> findById(Long id) {
        return postRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return postRepository.existsById(id);
    }

    public Posts save(Posts post) {
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
