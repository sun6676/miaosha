package com.example.seckilldemo.message;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public MessageProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void sendMessage(String topic, String message) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build());
        System.out.println("Sent message to topic " + topic + ": " + message);
    }
}
