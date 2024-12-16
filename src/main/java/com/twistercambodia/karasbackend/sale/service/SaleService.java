package com.twistercambodia.karasbackend.sale.service;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import com.twistercambodia.karasbackend.autoService.service.AutoServiceService;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import com.twistercambodia.karasbackend.sale.dto.ItemDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    private final AutoServiceService autoServiceService;

    public SaleService(
            SaleRepository saleRepository,
            ModelMapper modelMapper,
            UserService userService,
            CustomerService customerService,
            VehicleService vehicleService,
            UnitService unitService,
            AutoServiceService autoServiceService
    ) {
        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.unitService = unitService;
        this.autoServiceService = autoServiceService;
    }

    public List<Sale> findAll() {
        return this.saleRepository.findAll();
    }

    public Sale findByIdOrThrowException(String id) throws Exception {
        return this.saleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Sale not found with ID={}" + id)
        );
    }

    public Sale create(SaleRequestDto saleRequestDto) throws Exception {
        Sale sale = this.convertToSale(saleRequestDto);

        User user = this.userService.findByIdOrThrowError(saleRequestDto.getUserId());
        Customer customer = this.customerService.findByIdOrThrowError(saleRequestDto.getCustomerId());
        Vehicle vehicle = this.vehicleService.findByIdOrThrowException(saleRequestDto.getVehicleId());

        sale.setCreated(LocalDateTime.parse(saleRequestDto.getCreated()));
        sale.setDueDate(LocalDateTime.parse(saleRequestDto.getDueDate()));
        sale.setUser(user);
        sale.setCustomer(customer);
        sale.setVehicle(vehicle);

        List<Item> items = new ArrayList<>();

        for (ItemDto itemDto : saleRequestDto.getItems()) {
            Item item = this.modelMapper.map(itemDto, Item.class);

            if (itemDto.getUnitId() != null) {
                Unit unit = this.unitService.findByIdOrThrowError(itemDto.getUnitId());
                item.setUnit(unit);
            } else if (itemDto.getServiceId() != null) {
                AutoService service = this.autoServiceService.findByIdOrThrowError(itemDto.getServiceId());
                item.setService(service);
            }

            items.add(item);
        }

        sale.setItems(items);

        return this.saleRepository.save(sale);
    }

    public Sale update(String id, SaleRequestDto saleRequestDto) throws Exception {
        Sale sale = this.findByIdOrThrowException(id);

        User user = this.userService.findByIdOrThrowError(saleRequestDto.getUserId());
        Customer customer = this.customerService.findByIdOrThrowError(saleRequestDto.getCustomerId());
        Vehicle vehicle = this.vehicleService.findByIdOrThrowException(saleRequestDto.getVehicleId());

        sale.setCreated(LocalDateTime.parse(saleRequestDto.getCreated()));
        sale.setDueDate(LocalDateTime.parse(saleRequestDto.getDueDate()));
        sale.setUser(user);
        sale.setCustomer(customer);
        sale.setVehicle(vehicle);

        sale.getItems().clear();

        List<Item> items = new ArrayList<>();
        for (ItemDto itemDto : saleRequestDto.getItems()) {
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

    public List<SaleResponseDto> convertToSaleResponseDto(List<Sale> sales) {
        return sales
                .stream()
                .map((s) -> modelMapper.map(s, SaleResponseDto.class))
                .collect(Collectors.toList());
    }

    public SaleResponseDto convertToSaleResponseDto(Sale sale) {
        return this.modelMapper.map(sale, SaleResponseDto.class);
    }

    public Sale convertToSale(SaleRequestDto saleRequestDto) {
        return this.modelMapper.map(saleRequestDto, Sale.class);
    }
}
