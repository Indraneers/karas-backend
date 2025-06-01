package com.twistercambodia.karasbackend.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.sale.dto.SaleFilter;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final SaleService saleService;
    private final AuditService auditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public CustomerController(
            CustomerService customerService,
            VehicleService vehicleService,
            SaleService saleService,
            AuditService auditService,
            ObjectMapper objectMapper
    ) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.saleService = saleService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
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
        SaleFilter saleFilter = new SaleFilter();
        saleFilter.setCustomerId(id);
        return this.saleService.findAll(saleFilter, page)
                .map(saleService::convertToSaleResponseDto);
    }

    @GetMapping("{id}")
    public CustomerDto getCustomer(@PathVariable("id") String id) throws IOException {
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
            @RequestBody CustomerDto customerDto,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        Customer customer = this.customerService.create(customerDto);
        this.logger.info("Creating customer={}", customer);

        CustomerDto createdCustomer = this.customerService.convertToCustomerDto(customer);;

        // create audit log of Customer Creation
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(createdCustomer);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Customer Creation");
        auditDTO.setResourceName(customer.getName());
        auditDTO.setRequestUrl("/customers");
        auditDTO.setService(ServiceEnum.CUSTOMER);
        auditDTO.setHttpMethod(HttpMethod.POST);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for customer={}", audit);

        return createdCustomer;
    }

    @PutMapping("{id}")
    public CustomerDto updateCustomer(
            @RequestBody CustomerDto customerDto,
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Customer oldCustomer = this.customerService.findByIdOrThrowError(id);
        CustomerDto oldCustomerDto = this.customerService.convertToCustomerDto(oldCustomer);

        Customer customer = this.customerService.update(id, customerDto);
        this.logger.info("Updating customer={}", customer);

        CustomerDto updatedCustomerDto = this.customerService.convertToCustomerDto(customer);

        // create audit log of Customer Creation
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldCustomerDto);
        String newValueJSON = objectMapper.writeValueAsString(updatedCustomerDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Customer Update");
        auditDTO.setResourceName(customer.getName());
        auditDTO.setRequestUrl("/customers/" + id);
        auditDTO.setService(ServiceEnum.CUSTOMER);
        auditDTO.setHttpMethod(HttpMethod.PUT);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for customer={}", audit);

        return this.customerService.convertToCustomerDto(customer);
    }

    @DeleteMapping("{id}")
    public CustomerDto deleteCustomer(
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Customer oldCustomer = this.customerService.findByIdOrThrowError(id);
        CustomerDto oldCustomerDto = this.customerService.convertToCustomerDto(oldCustomer);

        Customer customer = this.customerService.delete(id);
        this.logger.info("Deleting customer={}", customer);

        CustomerDto deletedCustomerDto = this.customerService.convertToCustomerDto(customer);

        // create audit log of Customer Deleted
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldCustomerDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(null);

        auditDTO.setName("Customer Deletion");
        auditDTO.setResourceName(customer.getName());
        auditDTO.setRequestUrl("/customers/" + id);
        auditDTO.setService(ServiceEnum.CUSTOMER);
        auditDTO.setHttpMethod(HttpMethod.DELETE);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for customer={}", audit);

        return deletedCustomerDto;
    }
}
