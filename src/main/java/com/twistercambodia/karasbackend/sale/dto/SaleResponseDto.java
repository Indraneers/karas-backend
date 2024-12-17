package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.auth.dto.UserDto;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SaleResponseDto {
    private String id;
    private String created;
    private String dueDate;
    private int discount;
    private List<ItemResponseDto> items;
    private UserDto user;
    private CustomerDto customer;
    private VehicleDto vehicle;
    private SaleStatus status;

    public SaleResponseDto() {}

    public SaleResponseDto(Sale sale) {
        ModelMapper mapper = new ModelMapper();
        this.id = sale.getId();
        this.created = sale.getCreated().toString();
        this.dueDate = sale.getDueDate().toString();
        this.discount = sale.getDiscount();

        this.items = sale.getItems()
                .stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());

        this.user = mapper.map(sale.getUser(), UserDto.class);
        this.customer = mapper.map(sale.getCustomer(), CustomerDto.class);
        this.vehicle = mapper.map(sale.getVehicle(), VehicleDto.class);
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

    public List<ItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<ItemResponseDto> items) {
        this.items = items;
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
}
