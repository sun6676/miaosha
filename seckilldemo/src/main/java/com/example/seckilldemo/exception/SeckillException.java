package com.example.seckilldemo.exception;
import org.springframework.http.HttpStatus;
public class SeckillException extends RuntimeException {
    private final HttpStatus status;

    public SeckillException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

