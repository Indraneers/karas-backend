package com.twistercambodia.karasbackend.sale.controller;

import com.twistercambodia.karasbackend.sale.dto.SaleDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.service.SaleService;
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
    public List<SaleDto> getAllSales() {
        return this.saleService.convertToSaleDto(
                this.saleService.findAll()
        );
    }

    @GetMapping("{id}")
    public SaleDto getAllSales(
            @PathVariable("id") String id
    ) throws Exception {
        return this.saleService.convertToSaleDto(
                this.saleService.findByIdOrThrowException(id)
        );
    }

    @PostMapping
    public SaleDto createSale(
            @RequestBody SaleDto saleDto
    ) throws Exception {
        Sale sale = this.saleService.create(saleDto);
        this.logger.info("Creating Sale={}", sale);
        return this.saleService.convertToSaleDto(sale);
    }

    @PutMapping("{id}")
    public SaleDto updateSale(
            @PathVariable("id") String id,
            @RequestBody SaleDto saleDto
    ) throws Exception {
        Sale sale = this.saleService.update(id, saleDto);
        this.logger.info("Updating Sale={}", sale);
        return this.saleService.convertToSaleDto(sale);
    }

    @DeleteMapping("{id}")
    public SaleDto updateSale(
            @PathVariable("id") String id
    ) throws Exception {
        Sale sale = this.saleService.delete(id);
        this.logger.info("Deleting Sale={}", sale);
        return this.saleService.convertToSaleDto(sale);
    }
}
