package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.inventory.entity.Restock;

import java.util.List;

public class RestockResponseDto {
    private String id;
    private List<RestockItemResponseDto> items;
    private UserDto user;
    private String createdAt;

    public RestockResponseDto() {}

    public RestockResponseDto(Restock restock) {
        this.id = restock.getId();
        this.user = new UserDto(restock.getUser());
        this.createdAt = restock.getCreatedAt().toString();
        this.items = restock
                .getItems()
                .stream()
                .map(RestockItemResponseDto::new)
                .toList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RestockItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<RestockItemResponseDto> items) {
        this.items = items;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
