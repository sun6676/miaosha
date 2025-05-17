package com.example.seckilldemo.controller;
import com.alibaba.fastjson.JSON;
import com.example.seckilldemo.entity.Item;
import com.example.seckilldemo.entity.Order;
import com.example.seckilldemo.exception.SeckillException;
import com.example.seckilldemo.exception.SeckillStockException;
import com.example.seckilldemo.service.SeckillService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://192.168.0.131:5174")
@RequestMapping("/api/seckill")
public class SeckillController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private SeckillService seckillService;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return seckillService.getAllItems();
    }

    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return seckillService.getItemById(itemId);
    }

    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Long> orderInfo) {
        Long userId = orderInfo.get("userId");
        Long itemId = orderInfo.get("itemId");

        if (userId == null || itemId == null) {
            return new ResponseEntity<>(Map.of(
                    "status", "error",
                    "message", "Required parameters are missing: userId and itemId"
            ), HttpStatus.BAD_REQUEST);
        }

        try {
            // 准备订单信息（未创建订单实体）
            Map<String, Object> orderMessage = Map.of(
                    "userId", userId,
                    "itemId", itemId,
                    "status", "pending", // 标记为待处理状态
                    "timestamp", System.currentTimeMillis() // 添加时间戳
            );

            // 发送消息到消息队列（订单创建前）
            String orderMessageJson = JSON.toJSONString(orderMessage);
            System.out.println("准备发送订单预处理消息...");
            rocketMQTemplate.convertAndSend("seckill_order_topic", orderMessageJson);
            System.out.println("订单预处理消息已发送：" + orderMessageJson);

            // 创建订单实体
            Order savedOrder = seckillService.placeOrder(userId, itemId);

            // 更新订单状态（如果需要）
            Map<String, Object> updatedOrderMessage = Map.of(
                    "orderId", savedOrder.getId(),
                    "userId", userId,
                    "itemId", itemId,
                    "status", "created", // 标记为已创建状态
                    "timestamp", System.currentTimeMillis()
            );
            String updatedOrderMessageJson = JSON.toJSONString(updatedOrderMessage);
            rocketMQTemplate.convertAndSend("seckill_order_topic", updatedOrderMessageJson);
            System.out.println("订单更新消息已发送：" + updatedOrderMessageJson);

            // 输出订单号
            System.out.println("订单成功生成，订单号：" + savedOrder.getId());

            return new ResponseEntity<>(Map.of(
                    "status", "success",
                    "orderId", savedOrder.getId().toString()  // 显式转换为String
            ), HttpStatus.OK);
        } catch (SeckillStockException e) {
            return new ResponseEntity<>(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}