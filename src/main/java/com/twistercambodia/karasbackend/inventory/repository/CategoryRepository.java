package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, String> {
    @Query(value = "select c from Category c where lower(c.name) like lower(concat('%', concat(?1, '%')))")
    List<Category> findAllContaining(String q);

    @Query(value = "select c from Category c")
    List<Category> findAll();
}
