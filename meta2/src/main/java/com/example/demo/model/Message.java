package com.example.demo.model;

public record Message(String content) {
    public Message(String content) {
        this.content = content;
    }
}
