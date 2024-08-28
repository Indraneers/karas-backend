package com.twistercambodia.karasbackend.sale.controller;

import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.service.SaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sales")
public class SaleController {
    public final SaleService saleService;
    public SaleController(SaleService saleService, SaleService saleService1) {
        this.saleService = saleService1;
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return this.saleService.findAll();
    }
}
