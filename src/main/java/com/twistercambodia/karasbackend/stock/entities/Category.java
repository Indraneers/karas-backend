package com.twistercambodia.karasbackend.stock.entities;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name="category")
public class Category {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="product")
    private Product[] products;

    @Column(name="product_total")
    private int productTotal;

    // define constructors

    public Category() {}

    public Category(String name, Product[] products, int productTotal) {
        this.name = name;
        this.products = products;
        this.productTotal = productTotal;
    }

    // define getters/setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public int getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(int productTotal) {
        this.productTotal = productTotal;
    }

    // define toString() method

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + Arrays.toString(products) +
                ", productTotal=" + productTotal +
                '}';
    }
}
