package com.example.bankcards.entity;

public enum STATUS {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    EXPIRED("EXPIRED");
    private final String name;
    STATUS(String name) {
        this.name = name;
    }
    public String getName(){ return name; }
}
