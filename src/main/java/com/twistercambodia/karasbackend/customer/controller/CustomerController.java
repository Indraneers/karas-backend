package com.twistercambodia.karasbackend.customer.controller;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final SaleService saleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomerController(
            CustomerService customerService,
            VehicleService vehicleService,
            SaleService saleService
    ) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.saleService = saleService;
    }

    @GetMapping
    public Page<CustomerDto> getAllCustomers(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", required = true) int page
    ) {
        return this.customerService.findAll(q, page)
                .map(customerService::convertToCustomerDto);
    }

    @GetMapping("{id}/sales")
    public Page<SaleResponseDto> getCustomerSales(
            @PathVariable("id") String id,
            @RequestParam(value = "page", required = true) int page
    ) {
        return this.saleService.findAllByCustomerId(id, page)
                .map(saleService::convertToSaleResponseDto);
    }

    @GetMapping("{id}")
    public CustomerDto getCustomer(@PathVariable("id") String id) {
        return this.customerService.convertToCustomerDto(
                this.customerService.findByIdOrThrowError((id))
        );
    }

    @GetMapping("{id}/vehicles")
    public List<VehicleDto> getVehiclesByCustomerId(@PathVariable("id") String id) {
        return vehicleService.convertToVehicleDto(
                vehicleService.findByCustomerId(id)
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
