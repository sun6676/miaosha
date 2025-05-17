package com.example.seckilldemo.exception;

import org.springframework.http.HttpStatus;

public class SeckillLockException extends SeckillException {
    public SeckillLockException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS);
    }
}
