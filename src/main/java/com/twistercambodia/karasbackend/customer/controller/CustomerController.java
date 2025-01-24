package com.twistercambodia.karasbackend.customer.controller;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers(
            @RequestParam(value = "q", required = false) String q
    ) {
        return this.customerService.convertToCustomerDto(
                this.customerService.findAll(q)
        );
    }

    @GetMapping("{id}")
    public CustomerDto getCustomer(@PathVariable("id") String id) {
        return this.customerService.convertToCustomerDto(
                this.customerService.findByIdOrThrowError((id))
        );
    }

    @PostMapping
    public CustomerDto createCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        Customer customer = this.customerService.create(customerDto);
        this.logger.info("Creating customer={}", customer);
        return this.customerService.convertToCustomerDto(customer);
    }

    @PutMapping("{id}")
    public CustomerDto updateCustomer(
            @RequestBody CustomerDto customerDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Customer customer = this.customerService.update(id, customerDto);
        this.logger.info("Updating customer={}", customer);
        return this.customerService.convertToCustomerDto(customer);
    }

    @DeleteMapping("{id}")
    public CustomerDto deleteCustomer(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Customer customer = this.customerService.delete(id);
        this.logger.info("Deleting customer={}", customer);
        return this.customerService.convertToCustomerDto(customer);
    }
}
