package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.inventory.entity.Restock;

import java.util.List;

public class RestockRequestDto {
    private String id;
    private List<RestockItemRequestDto> items;
    private String userId;

    public RestockRequestDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RestockItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<RestockItemRequestDto> items) {
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
