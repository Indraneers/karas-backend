package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubcategoryRepository extends CrudRepository<Subcategory, String> {
    @Query("""
        select c from Subcategory c 
        where 
            (?1 is null or lower(cast(c.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
    """)
    List<Subcategory> findAll(String q);
}
