package com.twistercambodia.karasbackend.inventory.repositories;

import com.twistercambodia.karasbackend.inventory.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("select c from Category c")
    List<Category> findAll();
}
