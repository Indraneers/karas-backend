package com.twistercambodia.karasbackend.sale.controller;

import com.twistercambodia.karasbackend.sale.dto.SaleDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
