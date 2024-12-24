package com.twistercambodia.karasbackend.customer.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    @Query("""
        select c from Customer c
        where 
            (?1 is null or lower(cast(c.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
    """)
    List<Customer> findAll(String q);
}
