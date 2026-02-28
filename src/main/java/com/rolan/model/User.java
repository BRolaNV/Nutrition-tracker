package com.rolan.model;

public class User {
    private String userName;
    private int id;

    public User(String name, int id){
        this.userName = name;
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public int getId() {
        return id;
    }
}
