package com.twistercambodia.karasbackend.sale.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Sale sale;
}
