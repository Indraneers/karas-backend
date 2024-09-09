package com.twistercambodia.karasbackend.inventory.entities;

import jakarta.persistence.*;

@Entity
@Table(name="unit")
public class Unit {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String name;

    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column
    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // define toString() method

    @Override
    public String toString() {
        return "Unit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
