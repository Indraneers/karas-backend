package com.twistercambodia.karasbackend.customer.controller;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService, CustomerService customerService1) {
        this.customerService = customerService1;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return this.customerService.findAll();
    }
}
