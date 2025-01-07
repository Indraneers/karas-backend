package com.twistercambodia.karasbackend.inventory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(
            name="product_id")
    private List<Unit> units;

    @Formula("(select count(*) from unit u where u.product_id = id)")
    private int unitCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @Column
    private String baseUnit;

    @Column(nullable = false)
    private boolean variable;

    @Column
    private String img = "";

    // Getters/Setters

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

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitTotal) {
        this.unitCount = unitTotal;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public boolean isVariable() {
        return variable;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Product{" +
                "variable=" + variable +
                ", baseUnit='" + baseUnit + '\'' +
                ", unitCount=" + unitCount +
                ", units=" + units +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", img=" + img + '\'' +
                '}';
    }
}
