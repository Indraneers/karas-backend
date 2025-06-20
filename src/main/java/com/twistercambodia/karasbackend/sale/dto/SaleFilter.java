package com.twistercambodia.karasbackend.sale.dto;

import java.time.LocalDateTime;

public class SaleFilter {
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private String customerId;
    private String userId;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SaleFilter{" +
                "createdAtFrom=" + createdAtFrom +
                ", createdAtTo=" + createdAtTo +
                ", customerId='" + customerId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
