package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;

import java.util.List;
import java.util.stream.Collectors;

public class SaleRequestDto {
    private String id;
    private String created;
    private String dueDate;
    private int discount;
    private List<ItemRequestDto> items;
    private MaintenanceDto maintenance;
    private String userId;
    private String customerId;
    private String vehicleId;
    private SaleStatus status;

    public SaleRequestDto() {}

    public SaleRequestDto(Sale sale) {
        this.id = sale.getFormattedId();
        this.created = sale.getCreated().toString();
        this.dueDate = sale.getDueDate().toString();
        this.discount = sale.getDiscount();

        this.items = sale.getItems()
                .stream()
                .map(ItemRequestDto::new)
                .collect(Collectors.toList());

        if (sale.getMaintenance() != null) {
            this.maintenance = new MaintenanceDto(sale.getMaintenance());
        }

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

    public List<ItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ItemRequestDto> items) {
        this.items = items;
    }

    public MaintenanceDto getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(MaintenanceDto maintenance) {
        this.maintenance = maintenance;
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

    @Override
    public String toString() {
        return "SaleRequestDto{" +
                "id='" + id + '\'' +
                ", created='" + created + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", discount=" + discount +
                ", items=" + items +
                ", maintenance=" + maintenance +
                ", userId='" + userId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", status=" + status +
                '}';
    }
}
