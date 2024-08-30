package com.twistercambodia.karasbackend.customer.controller;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return this.customerService.convertToCustomerDto(
                this.customerService.findAll()
        );
    }

    @PostMapping
    public CustomerDto createCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        Customer customer = this.customerService.create(customerDto);
        return this.customerService.convertToCustomerDto(customer);
    }

    @PutMapping("{id}")
    public CustomerDto updateCustomer(
            @RequestBody CustomerDto customerDto,
            @PathVariable("id") String id
    ) throws Exception {
        Customer customer = this.customerService.update(id, customerDto);
        return this.customerService.convertToCustomerDto(customer);
    }
}
