package com.twistercambodia.karasbackend.sale.controller;

import com.twistercambodia.karasbackend.sale.dto.SaleRequestDto;
import com.twistercambodia.karasbackend.sale.dto.SaleResponseDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.entity.SaleStatus;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sales")
public class SaleController {
    private final SaleService saleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleResponseDto> getAllSales() {
        return this.saleService.convertToSaleResponseDto(
                this.saleService.findAll()
        );
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
            @RequestBody SaleRequestDto saleRequestDto
    ) throws Exception {
        Sale sale = this.saleService.create(saleRequestDto);
        this.logger.info("Creating Sale={}", sale);
        return this.saleService.convertToSaleResponseDto(sale);
    }

    @PutMapping("{id}")
    public SaleResponseDto updateSale(
            @PathVariable("id") String id,
            @RequestBody SaleRequestDto saleRequestDto
    ) throws Exception {
        Sale sale = this.saleService.update(id, saleRequestDto);
        this.logger.info("Updating Sale={}", sale);
        return this.saleService.convertToSaleResponseDto(sale);
    }

    @PutMapping("pay/{id}")
    public SaleResponseDto paySale(
            @PathVariable("id") String id
    ) throws Exception {
        Sale saleItem = this.saleService.findByIdOrThrowException(id);
        if (saleItem.getStatus() == SaleStatus.PAID) {
            return this.saleService.convertToSaleResponseDto(saleItem);
        }

        saleItem.setStatus(SaleStatus.PAID);

        Sale sale = this.saleService.update(
                saleItem.getFormattedId(),
                new SaleRequestDto(saleItem)
        );

        this.logger.info("Paying Sale={}", sale);
        return this.saleService.convertToSaleResponseDto(sale);
    }


    @DeleteMapping("{id}")
    public SaleResponseDto updateSale(
            @PathVariable("id") String id
    ) throws Exception {
        Sale sale = this.saleService.delete(id);
        this.logger.info("Deleting Sale={}", sale);
        return this.saleService.convertToSaleResponseDto(sale);
    }
}
