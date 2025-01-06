package com.twistercambodia.karasbackend.inventory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private List<Subcategory> subcategories;

    @Formula("(select count(*) from subcategory sc where sc.category_id = id)")
    private int subcategoryCount;

    @Column(nullable = true)
    private String img;

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

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public int getSubcategoryCount() {
        return subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subcategories=" + subcategories +
                ", subcategoryCount=" + subcategoryCount +
                ", img=" + img +
                '}';
    }
}
