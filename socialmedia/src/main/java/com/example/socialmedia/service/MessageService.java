package com.example.socialmedia.service;

import com.example.socialmedia.model.Messages;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Messages> getChatMessages(Long userId, Long otherUserId) {
        return messageRepository.findChatMessages(userId, otherUserId);
    }
    
    public List<User> getChatUsers(Long userId) {
        return messageRepository.findDistinctChatUsers(userId);
    }

    public List<Messages> findAll() {
        return messageRepository.findAll();
    }

    public Optional<Messages> findById(Long id) {
        return messageRepository.findById(id);
    }

    public Messages save(Messages message) {
        return messageRepository.save(message);
    }

    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    public List<User> getUsersWhoMessagedMe(Long receiverId) {
        return messageRepository.findDistinctSendersByReceiverId(receiverId);
    }
}
