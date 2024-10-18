package com.twistercambodia.karasbackend.vehicle.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, String> {
    @Query("SELECT v FROM Vehicle v")
    List<Vehicle> findAll();
}
