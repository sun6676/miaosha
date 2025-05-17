package com.example.seckilldemo.service;

import com.example.seckilldemo.entity.Item;
import com.example.seckilldemo.entity.Order;
import com.example.seckilldemo.exception.SeckillException;
import com.example.seckilldemo.exception.SeckillLockException;
import com.example.seckilldemo.exception.SeckillStockException;
import com.example.seckilldemo.exception.SeckillTimeException;
import com.example.seckilldemo.repository.ItemRepository;
import com.example.seckilldemo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new SeckillException("商品不存在", HttpStatus.NOT_FOUND));
    }

    // 预定义的Lua脚本（确保原子性）
    private static final String STOCK_LUA =
            "local stock = tonumber(redis.call('GET', KEYS[1])) \n" +
                    "if not stock or stock <= 0 then \n" +
                    "    return -1 \n" +
                    "end \n" +
                    "redis.call('DECR', KEYS[1]) \n" +
                    "return stock - 1";

    @Transactional
    public Order placeOrder(Long userId, Long itemId) throws SeckillException {
        // 获取分布式锁
        String lockKey = "seckill:lock:" + itemId;
        String lockValue = UUID.randomUUID().toString();

        try {
            // 获取锁
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
            if (locked == null || !locked) {
                throw new SeckillLockException("系统繁忙，请稍后再试");
            }

            // 检查商品是否存在
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new SeckillException("商品不存在", HttpStatus.NOT_FOUND));

            // 检查秒杀活动是否已经开始
            if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().isBefore(item.getStartTime().toInstant())) {
                throw new SeckillTimeException("秒杀未开始");
            }

            // Redis 库存扣减
            String luaScript =
                    "local stock = tonumber(redis.call('GET', KEYS[1]))\n" +
                            "if not stock or stock < 1 then\n" +
                            "    return -1\n" +
                            "end\n" +
                            "local new_stock = stock - 1\n" +
                            "if new_stock < 0 then\n" +
                            "    return -1\n" +
                            "end\n" +
                            "redis.call('SET', KEYS[1], new_stock)\n" +
                            "return new_stock";
            String key = "seckill:" + itemId;

            Long result = redisTemplate.execute(
                    new DefaultRedisScript<>(luaScript, Long.class),
                    Collections.singletonList(key)
            );
            System.out.println("Redis扣减结果: stock=" + result);
            if (!(result instanceof Long) || (Long) result < 0) {
                System.out.println("[WARN] 库存不足触发异常 (itemId=" + itemId + ")");
                throw new SeckillStockException("库存不足");
            }

            // 更新数据库中的库存（使用乐观锁）
            boolean dbUpdated = false;
            int maxRetries = 3;
            for (int attempt = 0; attempt < maxRetries; attempt++) {
                try {
                    int updateCount = itemRepository.updateStockByIdWithVersion(item.getId(), item.getVersion());
                    System.out.println("数据库更新结果: updateCount=" + updateCount);
                    if (updateCount > 0) {
                        // 直接使用 Redis 扣减后的库存值
                        redisTemplate.opsForValue().set(key, String.valueOf(result));
                        System.out.println("Redis同步后库存: " + redisTemplate.opsForValue().get(key));
                        dbUpdated = true;
                        break;
                    } else {
                        // 刷新版本号，重试
                        item = itemRepository.findById(itemId)
                                .orElseThrow(() -> new SeckillException("商品不存在", HttpStatus.NOT_FOUND));
                    }
                } catch (Exception e) {
                    if (attempt == maxRetries - 1) {
                        break;
                    }
                }
            }

            // 数据库更新失败时回滚Redis库存
            if (!dbUpdated) {
                redisTemplate.opsForValue().increment(key, 1);
                throw new SeckillException("库存更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 创建订单
            Order order = new Order();
            order.setUserId(userId);
            order.setItemId(itemId);
            order.setCreateTime(LocalDateTime.now());
            return orderRepository.save(order);

        }catch (SeckillLockException | SeckillStockException | SeckillTimeException e) {
            // 处理特定异常，例如记录日志
            System.err.println("发生异常：" + e.getMessage());
            throw e; // 如果希望继续向上层抛出异常，可以重新抛出
        } catch (Exception e) {
            // 捕获其他所有未预见的异常
            System.err.println("发生未知异常：" + e.getMessage());
            throw new SeckillException("服务器内部错误", HttpStatus.INTERNAL_SERVER_ERROR); // 转换为自定义异常
        } finally {
            // 释放分布式锁
            if (lockValue.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }
        }
    }
}