package com.twistercambodia.karasbackend.sale.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column
    private LocalDateTime dueDate;

    @Column
    private int discount;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @OneToOne(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Maintenance maintenance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private SaleStatus status;

    public Long getId() {
        return id;
    }

    public String getFormattedId() {
        return String.format("TW-%08d", id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", created=" + created +
                ", dueDate=" + dueDate +
                ", discount=" + discount +
                ", items=" + items +
                ", maintenance=" + maintenance +
                ", user=" + user +
                ", customer=" + customer +
                ", vehicle=" + vehicle.getId() +
                ", status=" + status +
                '}';
    }
}
