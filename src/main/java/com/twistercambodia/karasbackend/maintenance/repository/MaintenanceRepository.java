package com.twistercambodia.karasbackend.maintenance.repository;

import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaintenanceRepository extends CrudRepository<Maintenance, String> {
    @Query("select m from Maintenance m")
    List<Maintenance> findAll();
}
