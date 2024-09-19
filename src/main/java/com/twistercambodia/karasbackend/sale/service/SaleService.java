package com.twistercambodia.karasbackend.sale.service;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import com.twistercambodia.karasbackend.sale.dto.ItemDto;
import com.twistercambodia.karasbackend.sale.dto.SaleDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final UserService userService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;
    private final UnitService unitService;

    public SaleService(
            SaleRepository saleRepository,
            ModelMapper modelMapper,
            UserService userService,
            CustomerService customerService,
            VehicleService vehicleService,
            UnitService unitService
    ) {
        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.unitService = unitService;
    }

    public List<Sale> findAll() {
        return this.saleRepository.findAll();
    }

    public Sale findByIdOrThrowException(String id) throws Exception {
        return this.saleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Sale not found with ID={}" + id)
        );
    }

    public Sale create(SaleDto saleDto) throws Exception {
        Sale sale = this.convertToSale(saleDto);

        User user = this.userService.findByIdOrThrowError(saleDto.getUserId());
        Customer customer = this.customerService.findByIdOrThrowError(saleDto.getCustomerId());
        Vehicle vehicle = this.vehicleService.findByIdOrThrowException(saleDto.getVehicleId());

        sale.setCreated(LocalDateTime.parse(saleDto.getCreated()));
        sale.setDueDate(LocalDateTime.parse(saleDto.getDueDate()));
        sale.setUser(user);
        sale.setCustomer(customer);
        sale.setVehicle(vehicle);

        List<Item> items = new ArrayList<>();

        for (ItemDto itemDto : saleDto.getItems()) {
            Unit unit = this.unitService.findByIdOrThrowError(itemDto.getUnitId());
            Item item = this.modelMapper.map(itemDto, Item.class);

            item.setUnit(unit);

            items.add(item);
        }

        sale.setItems(items);

        return this.saleRepository.save(sale);
    }

    public Sale update(String id, SaleDto saleDto) throws Exception {
        Sale sale = this.findByIdOrThrowException(id);

        User user = this.userService.findByIdOrThrowError(saleDto.getUserId());
        Customer customer = this.customerService.findByIdOrThrowError(saleDto.getCustomerId());
        Vehicle vehicle = this.vehicleService.findByIdOrThrowException(saleDto.getVehicleId());

        sale.setCreated(LocalDateTime.parse(saleDto.getCreated()));
        sale.setDueDate(LocalDateTime.parse(saleDto.getDueDate()));
        sale.setUser(user);
        sale.setCustomer(customer);
        sale.setVehicle(vehicle);

        sale.getItems().clear();

        List<Item> items = new ArrayList<>();
        for (ItemDto itemDto : saleDto.getItems()) {
            Unit unit = this.unitService.findByIdOrThrowError(itemDto.getUnitId());
            Item item = this.modelMapper.map(itemDto, Item.class);

            item.setUnit(unit);
            items.add(item);
        }

        sale.getItems().addAll(items);

        return this.saleRepository.save(sale);
    }

    public Sale delete(String id) throws Exception {
        Sale sale = this.findByIdOrThrowException(id);
        this.saleRepository.delete(sale);
        return sale;
    }

    public SaleDto convertToSaleDto(Sale sale) {
        return this.modelMapper.map(sale, SaleDto.class);
    }

    public List<SaleDto> convertToSaleDto(List<Sale> sales) {
        return sales
                .stream()
                .map((s) -> modelMapper.map(s, SaleDto.class))
                .collect(Collectors.toList());
    }

    public Sale convertToSale(SaleDto saleDto) {
        return this.modelMapper.map(saleDto, Sale.class);
    }
}
