package com.twistercambodia.karasbackend.inventory.entities;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name="product")
public class Product {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Unit[] units;

    private int unitTotal;

    private Category category;

    // define constructors

    public Product() {}

    public Product(String name, Unit[] units, int unitTotal, Category category) {
        this.name = name;
        this.units = units;
        this.unitTotal = unitTotal;
        this.category = category;
    }

    // define getters/setters


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

    public Unit[] getUnits() {
        return units;
    }

    public void setUnits(Unit[] units) {
        this.units = units;
    }

    public int getUnitTotal() {
        return unitTotal;
    }

    public void setUnitTotal(int unitTotal) {
        this.unitTotal = unitTotal;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // define toString() method


    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", units=" + Arrays.toString(units) +
                ", unitTotal=" + unitTotal +
                ", category=" + category +
                '}';
    }
}
