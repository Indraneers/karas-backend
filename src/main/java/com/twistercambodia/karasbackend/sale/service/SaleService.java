package com.twistercambodia.karasbackend.sale.service;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import com.twistercambodia.karasbackend.autoService.service.AutoServiceService;
import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import com.twistercambodia.karasbackend.sale.dto.ItemRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import jakarta.transaction.Transactional;
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
    private final UnitService unitService;
    private final ModelMapper modelMapper;
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
        return this.saleRepository.findById(extractNumber(id)).orElseThrow(
                () -> new NotFoundException("Sale not found with ID={}" + id)
        );
    }

    @Transactional
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

        for (ItemRequestDto itemRequestDto : saleRequestDto.getItems()) {
            Item item = this.modelMapper.map(itemRequestDto, Item.class);

            if (itemRequestDto.getUnitId() != null) {
                Unit unit = this.unitService.findByIdOrThrowError(itemRequestDto.getUnitId());
                item.setUnit(unit);
            } else if (itemRequestDto.getServiceId() != null) {
                AutoService service = this.autoServiceService.findByIdOrThrowError(itemRequestDto.getServiceId());
                item.setService(service);
            }

            item.setSale(sale);
            items.add(item);
        }

        sale.setItems(items);

        Sale saleResult = this.saleRepository.save(sale);

        unitService.batchStockUpdate(saleResult.getItems(), StockUpdate.SALE);

        return saleResult;
    }

    @Transactional
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
        sale.setDiscount(saleRequestDto.getDiscount());
        sale.setStatus(saleRequestDto.getStatus());

        unitService.batchStockUpdate(sale.getItems(), StockUpdate.RESTOCK);

        sale.getItems().clear();

        List<Item> items = new ArrayList<>();

        for (ItemRequestDto itemRequestDto : saleRequestDto.getItems()) {
            Item item = this.modelMapper.map(itemRequestDto, Item.class);

            if (itemRequestDto.getUnitId() != null) {
                Unit unit = this.unitService.findByIdOrThrowError(itemRequestDto.getUnitId());
                item.setUnit(unit);
            } else if (itemRequestDto.getServiceId() != null) {
                AutoService service = this.autoServiceService.findByIdOrThrowError(itemRequestDto.getServiceId());
                item.setService(service);
            }

            item.setSale(sale);
            items.add(item);
        }

        sale.getItems().addAll(items);


        Sale saleResult = this.saleRepository.save(sale);
        unitService.batchStockUpdate(saleResult.getItems(), StockUpdate.SALE);

        return saleResult;
    }

    @Transactional
    public Sale delete(String id) throws Exception {
        Sale sale = this.findByIdOrThrowException(id);
        unitService.batchStockUpdate(sale.getItems(), StockUpdate.RESTOCK);
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

    public static Long extractNumber(String formattedId) {
        if (formattedId == null || !formattedId.startsWith("TW-") || formattedId.length() != 11) {
            throw new IllegalArgumentException("Invalid TW ID format: " + formattedId);
        }
        // Extract the numeric part (8 digits after "TW-")
        return Long.parseLong(formattedId.substring(3));
    }

}
