package com.example.seckilldemo;

import com.example.seckilldemo.entity.Item;
import com.example.seckilldemo.repository.ItemRepository;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

@SpringBootApplication
@Import(RocketMQAutoConfiguration.class)
public class SeckilldemoApplication {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public static void main(String[] args) {
        SpringApplication.run(SeckilldemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            Item item = new Item();
            item.setName("张张");
            item.setStock(10); // 设置初始库存
            item.setStartTime(new Date(System.currentTimeMillis() + 10 * 1000)); // 秒杀开始时间为当前时间后10秒
            Item savedItem = itemRepository.save(item);
            redisTemplate.opsForValue().set("seckill:" + savedItem.getId(), String.valueOf(savedItem.getStock()));
        };
    }
}