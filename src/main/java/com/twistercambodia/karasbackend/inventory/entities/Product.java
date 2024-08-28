package com.twistercambodia.karasbackend.inventory.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @OneToMany
    private List<Unit> units;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

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

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
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
                ", category=" + category +
                '}';
    }
}
