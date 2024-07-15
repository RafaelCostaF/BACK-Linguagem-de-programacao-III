package com.example.socialmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socialmedia.model.Friends;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.FriendsRepository;
import com.example.socialmedia.repository.UserRepository;

@Service
public class FriendsService {

    @Autowired
    private FriendsRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    public FriendsService(FriendsRepository friendRepository,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }


    public FriendsService() {
    }


    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("User cannot add themselves as a friend.");
        }

        // Check if the friendship already exists
        if (friendRepository.existsByUserIdAndFriendId(userId, friendId)) {
            throw new IllegalArgumentException("Friendship already exists.");
        }

        Friends friend = new Friends(userId,friendId);
        friendRepository.save(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        Friends friend = friendRepository.findByUserIdAndFriendId(userId, friendId);
        if (friend != null) {
            friendRepository.delete(friend);
        } else {
            throw new IllegalArgumentException("Friendship does not exist.");
        }
    }

    public List<User> getMyFriends(Long userId) {
        List<Long> friendIds = friendRepository.findFriendIdsByUserId(userId);
        return userRepository.findAllById(friendIds);
    }

    public List<User> getMyFollowers(Long userId) {
        List<Long> followerIds = friendRepository.findUserIdsByFriendId(userId);
        return userRepository.findAllById(followerIds);
    }
}