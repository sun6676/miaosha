package com.example.seckilldemo.message;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "skillTopic", consumerGroup = "my-consumer-group")
public class MessageConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("Received message from topic skillTopic: " + message);
    }
}