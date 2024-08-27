package com.twistercambodia.karasbackend.sale.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @OneToOne
    @JoinColumn(name="item_id")
    private Set<Item> items;

    @Column
    private int discount;


//    private User user;

//    private Customer customer;

//    private Set<Vehicle> vehicles;
//
//    private Status status;

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
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

//    public Set<Vehicle> getVehicles() {
//        return vehicles;
//    }
//
//    public void setVehicles(Set<Vehicle> vehicles) {
//        this.vehicles = vehicles;
//    }
//
//    public Status getStatus() {
//        return status;
//    }

//    public void setStatus(Status status) {
//        this.status = status;
//    }

    @Override
    public String toString() {
        return "Sale{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", dueDate=" + dueDate +
                ", discount=" + discount +
//                ", user=" + user +
//                ", customer=" + customer +
//                ", vehicles=" + vehicles +
//                ", status=" + status +
                '}';
    }
}
