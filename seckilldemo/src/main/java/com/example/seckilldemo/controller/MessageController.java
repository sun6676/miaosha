package com.example.seckilldemo.controller;

import com.example.seckilldemo.message.MessageProducer;
import com.example.seckilldemo.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageProducer messageProducer;

    @Autowired
    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @CrossOrigin(origins = "http://192.168.0.131:5174")
    @PostMapping("/send-message")
    public String sendMessage(@RequestBody MessageRequest request) {
        messageProducer.sendMessage("skillTopic", request.getMessage());
        return "Message sent to skillTopic: " + request.getMessage();
    }
}