package com.twistercambodia.karasbackend.auth.dto;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.entity.UserRole;

public class UserDto {
    private String id;
    private String username;
    private UserRole role;

    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
