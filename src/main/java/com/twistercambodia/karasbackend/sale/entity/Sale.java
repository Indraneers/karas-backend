package com.twistercambodia.karasbackend.sale.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale", schema = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private LocalDateTime dueDate;
}
