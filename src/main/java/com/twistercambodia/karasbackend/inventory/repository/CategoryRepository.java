package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, String> {
    @Query("""
        select c from Category c 
        where 
            (?1 is null or lower(cast(c.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
    """)
    List<Category> findAll(String q);
}
