package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.sale.entity.Sale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SaleDto {
    private String id;
    private LocalDateTime created;
    private LocalDateTime dueDate;
    private int discount;
    private List<ItemDto> items;
    private String userId;
    private String customerId;
    private String vehicleId;
    private StatusDto status;

    public SaleDto() {}

    public SaleDto(Sale sale) {
        this.id = sale.getId();
        this.created = sale.getCreated();
        this.dueDate = sale.getDueDate();
        this.discount = sale.getDiscount();

        this.items = sale.getItems()
                .stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());

        this.userId = sale.getUser().getId();
        this.customerId = sale.getCustomer().getId();
        this.vehicleId = sale.getVehicle().getId();

        this.status = new StatusDto(sale.getStatus());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
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
    
    public StatusDto getStatus() {
        return status;
    }

    public void setStatus(StatusDto status) {
        this.status = status;
    }
}
