package com.twistercambodia.karasbackend.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name="unit")
public class Unit {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private long quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column
    private int price;

    @Column
    private long toBaseUnit;

    @Transient
    public String getSubcategoryName() {
        return product.getSubcategory() != null ? product.getSubcategory().getName() : "";
    }

    @Transient
    public String getSubcategoryImg() {
        return product.getSubcategory() != null ? product.getSubcategory().getImg() : "";
    }

    @Transient
    public String getCategoryName() {
        return
                (
                    product.getSubcategory() != null
                    &&
                    product.getSubcategory().getCategory() != null
                )
                ? product.getSubcategory().getCategory().getName() : "";
    }


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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
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

    public long getToBaseUnit() {
        return toBaseUnit;
    }

    public void setToBaseUnit(long toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", toBaseUnit=" + toBaseUnit +
                '}';
    }
}
