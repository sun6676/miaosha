package com.example.seckilldemo.request;


import lombok.Data;

@Data
public class MessageRequest {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}