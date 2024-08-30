package com.twistercambodia.karasbackend.vehicle.repository;

import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<Vehicle, String> {
}
