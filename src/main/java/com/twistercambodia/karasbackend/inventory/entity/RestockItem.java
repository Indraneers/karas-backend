package com.twistercambodia.karasbackend.inventory.entity;

import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RestockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restock_id", nullable = false)
    private Restock restock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private StockUpdate status;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    public RestockItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Restock getRestock() {
        return restock;
    }

    public void setRestock(Restock restock) {
        this.restock = restock;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "RestockItem{" +
                "id='" + id + '\'' +
                ", restock=" + restock +
                ", unit=" + unit +
                ", quantity=" + quantity +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
