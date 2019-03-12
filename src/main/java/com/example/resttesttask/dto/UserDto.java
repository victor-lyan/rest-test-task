package com.example.resttesttask.dto;

import com.example.resttesttask.model.User;

public class UserDto {
    
    private int id;
    private String username;

    public UserDto(int id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
