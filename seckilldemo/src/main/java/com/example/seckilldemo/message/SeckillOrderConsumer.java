package com.example.seckilldemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RocketMQMessageListener(topic = "seckill_order_topic", consumerGroup = "seckill_consumer_group")
public class SeckillOrderConsumer implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(SeckillOrderConsumer.class);
    @Override
    public void onMessage(String message) {
        try {
            logger.info("接收到原始消息: {}", message);

            // 使用 TypeReference 解析消息
            Map<String, Object> orderMessage = parseMessage(message);

            String status = getString(orderMessage, "status");
            if ("pending".equals(status)) {
                handlePendingOrder(orderMessage);
            } else if ("created".equals(status)) {
                handleCreatedOrder(orderMessage);
            } else {
                logger.warn("未知消息类型: {}", message);
            }
        } catch (Exception e) {
            logger.error("处理订单消息时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 使用 TypeReference 解析消息
     */
    private Map<String, Object> parseMessage(String message) {
        return JSON.parseObject(message, new TypeReference<>() {
        });
    }

    /**
     * 安全地从 Map 中获取 String 类型的值
     */
    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof String ? (String) value : null;
    }

    /**
     * 安全地从 Map 中获取 Long 类型的值
     */
    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    /**
     * 处理预处理订单消息
     */
    private void handlePendingOrder(Map<String, Object> orderMessage) {
        logger.info("处理预处理订单消息: {}", orderMessage);

        Long userId = getLong(orderMessage, "userId");
        Long itemId = getLong(orderMessage, "itemId");

        System.out.println("预处理订单 - 用户ID: " + userId + ", 商品ID: " + itemId);
    }

    /**
     * 处理订单创建完成消息
     */
    private void handleCreatedOrder(Map<String, Object> orderMessage) {
        logger.info("处理已创建订单消息: {}", orderMessage);

        Long orderId = getLong(orderMessage, "orderId");
        Long userId = getLong(orderMessage, "userId");
        Long itemId = getLong(orderMessage, "itemId");

        System.out.println("订单已创建 - 订单ID: " + orderId + ", 用户ID: " + userId + ", 商品ID: " + itemId);
    }
}