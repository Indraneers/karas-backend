package com.twistercambodia.karasbackend.sale.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_type")
public class ItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // private Unit unit;

    // private Service service;


}
