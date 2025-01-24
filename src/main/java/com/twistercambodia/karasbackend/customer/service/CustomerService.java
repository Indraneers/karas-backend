package com.twistercambodia.karasbackend.customer.service;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.repository.CustomerRepository;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public List<Customer> findAll(String query) {
        return this.customerRepository.findAll(query);
    }

    public Customer findByIdOrThrowError(String id) throws RuntimeException {
        return this.customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Customer Not Found with ID=" + id));
    }

    public Customer create(CustomerDto customerDto) {
        Customer customer = this.convertToCustomer(customerDto);
        return this.customerRepository.save(customer);
    }

    public Customer update(String id, CustomerDto customerDto) throws RuntimeException {
        Customer customer = findByIdOrThrowError(id);

        customer.setName(customerDto.getName());
        customer.setNote(customerDto.getNote());
        customer.setAddress(customerDto.getAddress());
        customer.setContact(customerDto.getContact());

        return this.customerRepository.save(customer);
    }

    public Customer delete(String id) throws RuntimeException {
        Customer customer = this.findByIdOrThrowError(id);

        this.customerRepository.delete(customer);
        return customer;
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

    public Customer convertToCustomer(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
}
