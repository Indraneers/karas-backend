package com.twistercambodia.karasbackend.sale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.sale.dto.SaleFilter;
import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("sales")
public class SaleController {
    private final SaleService saleService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SaleController(
            SaleService saleService,
            AuditService auditService,
            ObjectMapper objectMapper
    ) {
        this.saleService = saleService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    private static String getSaleResourceName(Sale sale) {
        return sale.getCustomer().getName()
                +
                " - "
                +
                sale.getVehicle().getMakeAndModel()
                +
                (
                        sale.getVehicle().getPlateNumber().isEmpty()
                        ?
                        (" (" + sale.getVehicle().getPlateNumber() + ')')
                        : ""
                );

    }

    @GetMapping
    public Page<SaleResponseDto> getAllSales(
            @RequestParam() int page,
            SaleFilter saleFilter
            ) {
        Page<Sale> sales = this.saleService.findAll(saleFilter, page);
        return sales
                .map(saleService::convertToSaleResponseDto);
    }

    @GetMapping("{id}")
    public SaleResponseDto getSaleById(
            @PathVariable("id") String id
    ) throws Exception {
        return this.saleService.convertToSaleResponseDto(
                this.saleService.findByIdOrThrowException(id)
        );
    }

    @PostMapping
    public SaleResponseDto createSale(
            @RequestBody SaleRequestDto saleRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) throws Exception {
        Sale sale = this.saleService.create(saleRequestDto);
        this.logger.info("Creating Sale={}", sale);

        SaleResponseDto saleResponseDto = this.saleService.convertToSaleResponseDto(sale);

        // create audit log of Sale created
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(saleResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Sale Creation");
        auditDTO.setResourceName(getSaleResourceName(sale));
        auditDTO.setRequestUrl("/sales");
        auditDTO.setService(ServiceEnum.SALE);
        auditDTO.setHttpMethod(HttpMethod.POST);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for sale={}", audit);

        return saleResponseDto;
    }

    @PutMapping("{id}")
    public SaleResponseDto updateSale(
            @PathVariable("id") String id,
            @RequestBody SaleRequestDto saleRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) throws Exception {
        Sale oldSale = this.saleService.findByIdOrThrowException(id);
        Sale sale = this.saleService.update(id, saleRequestDto);
        this.logger.info("Updating Sale={}", sale);

        SaleResponseDto oldSaleResponseDto = this.saleService.convertToSaleResponseDto(oldSale);
        SaleResponseDto saleResponseDto = this.saleService.convertToSaleResponseDto(sale);

        // create audit log of Sale updated
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldSaleResponseDto);
        String newValueJSON = objectMapper.writeValueAsString(saleResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Sale Updated");
        auditDTO.setResourceName(getSaleResourceName(sale));
        auditDTO.setRequestUrl("/sales/" + id);
        auditDTO.setService(ServiceEnum.SALE);
        auditDTO.setHttpMethod(HttpMethod.PUT);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for sale={}", audit);

        return saleResponseDto;
    }

    @PutMapping("pay/{id}")
    public SaleResponseDto paySale(
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws Exception {
        Sale oldSale = this.saleService.findByIdOrThrowException(id);

        if (oldSale.getStatus() == SaleStatus.PAID) {
            return this.saleService.convertToSaleResponseDto(oldSale);
        }

        oldSale.setStatus(SaleStatus.PAID);

        Sale sale = this.saleService.update(
                oldSale.getFormattedId(),
                new SaleRequestDto(oldSale)
        );

        this.logger.info("Paying Sale={}", sale);

        SaleResponseDto oldSaleResponseDto = this.saleService.convertToSaleResponseDto(oldSale);
        SaleResponseDto saleResponseDto = this.saleService.convertToSaleResponseDto(sale);

        // create audit log of Sale paid
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldSaleResponseDto);
        String newValueJSON = objectMapper.writeValueAsString(saleResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Sale Paid");
        auditDTO.setResourceName(getSaleResourceName(sale));
        auditDTO.setRequestUrl("/sales/" + id);
        auditDTO.setService(ServiceEnum.SALE);
        auditDTO.setHttpMethod(HttpMethod.PUT);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for sale={}", audit);

        return saleResponseDto;
    }


    @DeleteMapping("{id}")
    public SaleResponseDto deleteSale(
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws Exception {
        Sale oldSale = this.saleService.findByIdOrThrowException(id);
        Sale sale = this.saleService.delete(id);
        this.logger.info("Deleting Sale={}", sale);

        SaleResponseDto oldSaleResponseDto = this.saleService.convertToSaleResponseDto(oldSale);
        SaleResponseDto saleResponseDto = this.saleService.convertToSaleResponseDto(sale);

        // create audit log of Sale updated
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldSaleResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(null);

        auditDTO.setName("Sale Deletion");
        auditDTO.setResourceName(getSaleResourceName(sale));
        auditDTO.setRequestUrl("/sales/" + id);
        auditDTO.setService(ServiceEnum.SALE);
        auditDTO.setHttpMethod(HttpMethod.DELETE);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        this.logger.info("Adding audit log for sale={}", audit);

        return saleResponseDto;
    }
}
