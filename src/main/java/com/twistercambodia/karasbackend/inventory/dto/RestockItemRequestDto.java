package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Restock;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;

public class RestockItemRequestDto {
    private String unitId;
    private int quantity;
    private StockUpdate status;

    public RestockItemRequestDto() {}

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockUpdate getStatus() {
        return status;
    }

    public void setStatus(StockUpdate status) {
        this.status = status;
    }
}
