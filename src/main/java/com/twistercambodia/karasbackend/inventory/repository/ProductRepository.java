package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {
    @Query(
        """
        select p from Product p
        where
            (?1 is null or lower(cast(p.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
            AND
            (?2 is null or p.category.id = ?2)
        """
    )
    List<Product> findAll(String q, String categoryId);
}
