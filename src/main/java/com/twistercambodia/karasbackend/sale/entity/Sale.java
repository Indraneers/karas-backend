package com.twistercambodia.karasbackend.sale.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private LocalDateTime dueDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="sale_id")
    private List<Item> items;

    @Column
    private int discount;

//    private User user;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    //private Set<Vehicle> vehicles;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Status> status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

//    public Set<Vehicle> getVehicles() {
//        return vehicles;
//    }
//
//    public void setVehicles(Set<Vehicle> vehicles) {
//        this.vehicles = vehicles;
//    }
//

    public Set<Status> getStatus() {
        return status;
    }

    public void setStatus(Set<Status> status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", dueDate=" + dueDate +
                ", items=" + items +
                ", discount=" + discount +
                ", customer=" + customer +
                ", status=" + status +
                '}';
    }
}
