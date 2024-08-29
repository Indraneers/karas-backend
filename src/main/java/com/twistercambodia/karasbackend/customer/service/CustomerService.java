package com.twistercambodia.karasbackend.customer.service;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public CustomerDto convertToCustomerDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    public List<CustomerDto> convertToCustomerDto(List<Customer> customers) {
        return customers
                .stream()
                .map((customer) -> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    public List<Customer> findAll() {
        return this.customerRepository.findAll();
    }
}
