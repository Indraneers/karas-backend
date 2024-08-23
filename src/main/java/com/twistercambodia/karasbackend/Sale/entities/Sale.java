package com.twistercambodia.karasbackend.Sale.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SALE", schema = "SALE")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private LocalDateTime dueDate;
}
