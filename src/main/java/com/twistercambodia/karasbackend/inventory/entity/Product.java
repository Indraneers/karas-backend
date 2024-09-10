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
    private List<Unit> units;

    @Formula("(select count(*) from unit u where u.product_id = id)")
    private int unitTotal;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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
                ", units=" + units +
                ", unitTotal=" + unitTotal +
                ", category=" + category +
                '}';
    }
}
