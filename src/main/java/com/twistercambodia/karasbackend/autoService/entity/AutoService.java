package com.twistercambodia.karasbackend.autoService.entity;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class AutoService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isActive;

    @Column
    private int originalPrice;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    public AutoService() {}

    public AutoService(AutoServiceDto autoServiceDto) {
        this.id = autoServiceDto.getId();
        this.name = autoServiceDto.getName();
        this.isActive = autoServiceDto.isActive();
        this.originalPrice = autoServiceDto.getOriginalPrice();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", originalPrice=" + originalPrice +
                '}';
    }
}
