package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Restock;
import com.twistercambodia.karasbackend.inventory.entity.RestockItem;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;

public class RestockItemResponseDto {
    private String id;
    private UnitRequestDto unit;
    private int quantity;
    private StockUpdate stockUpdate;

    public RestockItemResponseDto() {}
    public RestockItemResponseDto(RestockItem restockItem) {
        this.id = restockItem.getId();
        this.unit = new UnitRequestDto(restockItem.getUnit());
        this.quantity = restockItem.getQuantity();
        this.stockUpdate = restockItem.getStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UnitRequestDto getUnit() {
        return unit;
    }

    public void setUnit(UnitRequestDto unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockUpdate getStockUpdate() {
        return stockUpdate;
    }

    public void setStockUpdate(StockUpdate stockUpdate) {
        this.stockUpdate = stockUpdate;
    }
}
