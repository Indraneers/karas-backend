package com.twistercambodia.karasbackend.customer.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c")
    List<Customer> findAll();
}
