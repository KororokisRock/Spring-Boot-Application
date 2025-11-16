package com.example.bankcards.dto;

public class MessageDTO {
    private String message;

    public MessageDTO(String m) {
        message = m;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String m) {
        message = m;
    }

}
