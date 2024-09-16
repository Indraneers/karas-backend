package com.twistercambodia.karasbackend.sale.service;

import com.twistercambodia.karasbackend.sale.dto.SaleDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;

    public SaleService(SaleRepository saleRepository, ModelMapper modelMapper) {

        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
    }

    public List<Sale> findAll() {
        return this.saleRepository.findAll();
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
}
