package com.example.socialmedia.repository;

import com.example.socialmedia.model.Posts;
import com.example.socialmedia.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    boolean existsById(Long id);
    List<Posts> findByUser(User user);
}
