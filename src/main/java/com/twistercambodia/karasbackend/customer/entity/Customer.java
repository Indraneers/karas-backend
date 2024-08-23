package com.twistercambodia.karasbackend.customer.entity;

import jakarta.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    public String id;

    @Column(unique = true, nullable = true)
    public String name;

    @Column
    public String note;
}
