package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Restock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestockRepository extends CrudRepository<Restock, String> {
    @Query("select r from Restock r")
    List<Restock> findAll();
}
