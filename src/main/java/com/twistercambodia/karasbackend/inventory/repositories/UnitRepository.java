package com.twistercambodia.karasbackend.inventory.repositories;

import com.twistercambodia.karasbackend.inventory.entities.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit, String> {
    @Query("select u from Unit u")
    List<Unit> findAll();
}
