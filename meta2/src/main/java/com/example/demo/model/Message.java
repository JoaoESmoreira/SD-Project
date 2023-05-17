package com.example.demo.model;

public record Message(String content) {
    /*@Override
    public String content(String message) {
        StringBuilder sms = new StringBuilder();
        sms.append(message);
        sms.reverse();

        System.out.println("here");
        System.out.println(sms.toString());
        System.out.println("here1");
        return sms.toString();
    }*/
    public Message(String content) {
        // StringBuilder sms = new StringBuilder();
        // sms.append(content);
        // sms.reverse();
        // this.content = sms.toString();
        this.content = content;
    }
}
