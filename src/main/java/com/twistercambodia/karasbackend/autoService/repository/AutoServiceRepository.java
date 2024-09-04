package com.twistercambodia.karasbackend.autoService.repository;

import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AutoServiceRepository extends CrudRepository<AutoService, String> {
    @Query("select s from AutoService s")
    List<AutoService> findAll();
}
