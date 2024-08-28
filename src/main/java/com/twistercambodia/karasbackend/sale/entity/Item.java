package com.twistercambodia.karasbackend.sale.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // private ItemType itemType;

    //private Unit unit;

    @Column
    private int price;

    @Column
    private int quantity;

    @Column
    private int discount;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
