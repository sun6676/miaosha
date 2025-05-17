package com.example.seckilldemo.exception;

import org.springframework.http.HttpStatus;

// 业务子异常类
public class SeckillStockException extends SeckillException {
    public SeckillStockException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
