package com.twistercambodia.karasbackend.sale.dto;

import java.time.LocalDateTime;

public class SaleFilter {
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;

    public LocalDateTime getCreatedAtFrom() {
        return createdAtFrom;
    }

    public void setCreatedAtFrom(LocalDateTime createdAtFrom) {
        this.createdAtFrom = createdAtFrom;
    }

    public LocalDateTime getCreatedAtTo() {
        return createdAtTo;
    }

    public void setCreatedAtTo(LocalDateTime createdAtTo) {
        this.createdAtTo = createdAtTo;
    }
}
