package com.twistercambodia.karasbackend.inventory.repositories;

import com.twistercambodia.karasbackend.inventory.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {
    @Query("select p, size(p.units) as unitTotal from Product p")
    List<Product> findAll();
}
