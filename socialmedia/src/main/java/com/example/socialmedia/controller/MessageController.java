package com.example.socialmedia.controller;

import com.example.socialmedia.model.Message;
import com.example.socialmedia.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @Operation(summary = "Get all messages")
    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by id")
    public Optional<Message> getMessageById(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.save(message);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing message")
    public Message updateMessage(@PathVariable Long id, @RequestBody Message message) {
        message.setId(id);
        return messageService.save(message);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a message")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteById(id);
    }
}
