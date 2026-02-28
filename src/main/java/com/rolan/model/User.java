package com.rolan.model;

public class User {
    private String userName;
    private int id;
    private Long chatId;

    public User(String name, int id, Long chatId) {
        this.userName = name;
        this.id = id;
        this.chatId = chatId;
    }

    public String getUserName(){
        return userName;
    }

    public int getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }
}
