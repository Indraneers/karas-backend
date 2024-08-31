package com.twistercambodia.karasbackend.sale.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToMany
    private Set<Sale> sales;
}
