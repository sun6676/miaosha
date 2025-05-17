package com.example.seckilldemo.exception;

import org.springframework.http.HttpStatus;

public class SeckillTimeException extends SeckillException {
    public SeckillTimeException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
