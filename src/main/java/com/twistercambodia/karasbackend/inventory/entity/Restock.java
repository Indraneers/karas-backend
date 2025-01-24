package com.twistercambodia.karasbackend.inventory.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Restock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "restock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestockItem> items;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="user_id"
    )
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RestockItem> getItems() {
        return items;
    }

    public void setItems(List<RestockItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
