package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.sale.entity.PaymentType;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SaleResponseDto {
    private String id;
    private String createdAt;
    private String dueAt;
    private int discount;
    private List<ItemResponseDto> items;
    private MaintenanceDto maintenance;
    private UserDto user;
    private CustomerDto customer;
    private VehicleDto vehicle;
    private SaleStatus status;
    private PaymentType paymentType;

    public SaleResponseDto() {}

    public SaleResponseDto(Sale sale) {
        ModelMapper mapper = new ModelMapper();
        this.id = sale.getFormattedId();
        this.createdAt = sale.getCreatedAt().toString();
        this.dueAt = sale.getDueAt().toString();
        this.discount = sale.getDiscount();

        this.items = sale.getItems()
                .stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());

        if (sale.getMaintenance() != null) {
            this.maintenance = new MaintenanceDto(sale.getMaintenance());
        }
        this.user = mapper.map(sale.getUser(), UserDto.class);
        this.customer = mapper.map(sale.getCustomer(), CustomerDto.class);
        this.vehicle = mapper.map(sale.getVehicle(), VehicleDto.class);
        this.status = sale.getStatus();
        this.paymentType = sale.getPaymentType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<ItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<ItemResponseDto> items) {
        this.items = items;
    }

    public MaintenanceDto getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(MaintenanceDto maintenance) {
        this.maintenance = maintenance;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
