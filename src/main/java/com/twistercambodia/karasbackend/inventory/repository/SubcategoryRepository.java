package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubcategoryRepository extends CrudRepository<Subcategory, String> {
    @Query("""
        select sc from Subcategory sc 
        where 
            (?1 is null or lower(cast(sc.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
            AND
            (?2 is null or sc.category.id = ?2)
    """)
    List<Subcategory> findAll(String q, String categoryId);
}
