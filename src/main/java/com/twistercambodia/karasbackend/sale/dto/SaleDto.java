package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SaleDto {
    private String id;
    private String created;
    private String dueDate;
    private int discount;
    private List<ItemDto> items;
    private String userId;
    private String customerId;
    private String vehicleId;
    private SaleStatus status;

    public SaleDto() {}

    public SaleDto(Sale sale) {
        this.id = sale.getId();
        this.created = sale.getCreated().toString();
        this.dueDate = sale.getDueDate().toString();
        this.discount = sale.getDiscount();

        this.items = sale.getItems()
                .stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());

        this.userId = sale.getUser().getId();
        this.customerId = sale.getCustomer().getId();
        this.vehicleId = sale.getVehicle().getId();
        this.status = sale.getStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
}
