package com.example.socialmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.socialmedia.model.Friends;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
    Friends findByUserIdAndFriendId(Long userId, Long friendId);

    @Query("SELECT f.friendId FROM Friends f WHERE f.userId = :userId")
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.userId FROM Friends f WHERE f.friendId = :friendId")
    List<Long> findUserIdsByFriendId(@Param("friendId") Long friendId);
}