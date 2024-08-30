package com.twistercambodia.karasbackend.customer.controller;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.exception.CustomerNotFoundException;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
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
    ) throws RuntimeException {
        Customer customer = this.customerService.update(id, customerDto);
        return this.customerService.convertToCustomerDto(customer);
    }

    @DeleteMapping("{id}")
    public CustomerDto deleteCustomer(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Customer customer = this.customerService.delete(id);
        return this.customerService.convertToCustomerDto(customer);
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFound(CustomerNotFoundException exception) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );
    }
}
