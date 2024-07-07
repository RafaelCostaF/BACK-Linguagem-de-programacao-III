package com.example.socialmedia.repository;

import com.example.socialmedia.model.Messages;
import com.example.socialmedia.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query("SELECT DISTINCT m.sender FROM Messages m WHERE m.receiver.id = :receiverId")
    List<User> findDistinctSendersByReceiverId(@Param("receiverId") Long receiverId);

    @Query("SELECT DISTINCT m.sender FROM Messages m WHERE m.receiver.id = :userId " +
    "UNION " +
    "SELECT DISTINCT m.receiver FROM Messages m WHERE m.sender.id = :userId")
    List<User> findDistinctChatUsers(@Param("userId") Long userId);


    @Query("SELECT m FROM Messages m WHERE " +
           "(m.sender.id = :userId AND m.receiver.id = :otherUserId) OR " +
           "(m.sender.id = :otherUserId AND m.receiver.id = :userId) " +
           "ORDER BY m.sentAt")
    List<Messages> findChatMessages(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);
}