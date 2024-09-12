package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.Status;

public class StatusDto {
    private long id;
    private String name;

    public StatusDto() {}

    public StatusDto(Status status) {
        this.id = status.getId();
        this.name = status.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
