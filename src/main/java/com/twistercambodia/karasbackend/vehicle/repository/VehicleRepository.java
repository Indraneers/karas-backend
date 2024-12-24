package com.twistercambodia.karasbackend.vehicle.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, String> {
    @Query(
    """
    select v from Vehicle v
        where
        (
            (?1 is null or lower(cast(v.makeAndModel as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
            OR 
            (?1 is null or lower(cast(v.plateNumber as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
            OR 
            (?1 is null or lower(cast(v.customer.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
        )
    """)
    List<Vehicle> findAll(String q);

    @Query(
        """
        select v from Vehicle v where (?1 = v.customer.id)
        """
    )
    List<Vehicle> findByCustomerId(@Param("customerId") String customerId);
}
