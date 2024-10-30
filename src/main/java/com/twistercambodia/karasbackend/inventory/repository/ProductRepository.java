package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {
    @Query("select p from Product p")
    List<Product> findAll();
}
