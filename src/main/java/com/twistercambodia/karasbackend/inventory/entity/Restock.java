package com.twistercambodia.karasbackend.inventory.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
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
    private Instant createdAt;

    @Column()
    private Instant updatedAt;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Restock{" +
                "id='" + id + '\'' +
                ", items=" + items +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
