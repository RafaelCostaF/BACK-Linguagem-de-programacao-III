package com.example.socialmedia.controller;

import com.example.socialmedia.dtos.MessageDto;
import com.example.socialmedia.dtos.MessageRequestDto;
import com.example.socialmedia.dtos.UserDto;
import com.example.socialmedia.model.Messages;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("/messages")
@Tag(name = "Message Management", description = "Operations pertaining to message management")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;


    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/to/{receiverId}")
    @Operation(summary = "Send a message to another user")
    public ResponseEntity<MessageDto> sendMessage(@PathVariable Long receiverId, @RequestBody MessageRequestDto messageRequest) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional <User> receiverOptional = userRepository.findById(receiverId);
        if (!receiverOptional.isPresent()){
            return (ResponseEntity<MessageDto>) ResponseEntity.notFound();
        } 

        User receiver = receiverOptional.get();

        byte[] image;
        if (messageRequest.getImageBase64() != null){
            image = Base64.getDecoder().decode(messageRequest.getImageBase64());
    
            // Check if the uploaded file is a valid image
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            if (bufferedImage == null) {
                image = null; // Setting image to null when error case
            }
        } else { 
            image = null;
        }

        Messages message = new Messages(currentUser, receiver, messageRequest.getContent(), image);

        message = messageService.save(message);

        MessageDto messageDto;

        if (message.getImage() != null) {
            messageDto = new MessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                Base64.getEncoder().encodeToString(message.getImage()),
                message.getSentAt()
            );
        } else {
            messageDto = new MessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                null,
                message.getSentAt()
            );
        }

        return ResponseEntity.ok(messageDto);
    }


    @GetMapping("/chats")
    @Operation(summary = "Get users who have chatted with the authenticated user")
    public ResponseEntity<List<UserDto>> getChatUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<User> users = messageService.getChatUsers(currentUser.getId());

        List<UserDto> userDtos = users.stream()
            .map(user -> new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/chats/{userId}")
    @Operation(summary = "Get chat messages with a specific user")
    public ResponseEntity<List<MessageDto>> getChatWithUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Messages> messages = messageService.getChatMessages(currentUser.getId(), userId);

        List<MessageDto> messageDtos = messages.stream()
            .map(message -> new MessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getImage() != null ? Base64.getEncoder().encodeToString(message.getImage()) : null,
                message.getSentAt()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(messageDtos);
    }
    
    
        // @GetMapping("/users_messaged_me")
        // @Operation(summary = "Get users who sent messages to the authenticated user")
        // public ResponseEntity<List<UserDto>> getUsersWhoMessagedMe() {
        //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //     User currentUser = (User) authentication.getPrincipal();
    
        //     List<User> users = messageService.getUsersWhoMessagedMe(currentUser.getId());
    
        //     List<UserDto> userDtos = users.stream()
        //         .map(user -> new UserDto(
        //             user.getId(),
        //             user.getUsername(),
        //             user.getFullName(),
        //             user.getImage() != null ? Base64.getEncoder().encodeToString(user.getImage()) : null
        //         ))
        //         .collect(Collectors.toList());
    
        //     return ResponseEntity.ok(userDtos);
        // }
    
    // @GetMapping
    // @Operation(summary = "Get all messages")
    // public List<Message> getAllMessages() {
    //     return messageService.findAll();
    // }

    // @GetMapping("/{id}")
    // @Operation(summary = "Get message by id")
    // public Optional<Message> getMessageById(@PathVariable Long id) {
    //     return messageService.findById(id);
    // }

    // @PostMapping
    // @Operation(summary = "Create a new message")
    // public Message createMessage(@RequestBody Message message) {
    //     return messageService.save(message);
    // }

    // @PutMapping("/{id}")
    // @Operation(summary = "Update an existing message")
    // public Message updateMessage(@PathVariable Long id, @RequestBody Message message) {
    //     message.setId(id);
    //     return messageService.save(message);
    // }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete a message")
    // public void deleteMessage(@PathVariable Long id) {
    //     messageService.deleteById(id);
    // }
}
