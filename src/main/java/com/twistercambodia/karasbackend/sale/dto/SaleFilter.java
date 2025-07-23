package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.PaymentType;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;

import java.time.Instant;

public class SaleFilter {
    private Instant createdAtFrom;
    private Instant createdAtTo;
    private String customerId;
    private String vehicleId;
    private String userId;
    private SaleStatus status;
    private PaymentType paymentType;

    public Instant getCreatedAtFrom() {
        return createdAtFrom;
    }

    public void setCreatedAtFrom(Instant createdAtFrom) {
        this.createdAtFrom = createdAtFrom;
    }

    public Instant getCreatedAtTo() {
        return createdAtTo;
    }

    public void setCreatedAtTo(Instant createdAtTo) {
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

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus saleStatus) {
        this.status = saleStatus;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "SaleFilter{" +
                "createdAtFrom=" + createdAtFrom +
                ", createdAtTo=" + createdAtTo +
                ", customerId='" + customerId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", paymentType=" + paymentType +
                '}';
    }
}
