package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableUnit;
import com.twistercambodia.karasbackend.inventory.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public UnitService(UnitRepository unitRepository, ProductService productService, ModelMapper modelMapper) {
        this.unitRepository = unitRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public List<Unit> findAll(String query, String productId) {
        return this.unitRepository.findAll(query, productId);
    }

    public Unit findByIdOrThrowError(String id) throws RuntimeException {
        return this.unitRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Unit Not Found with ID=" + id));
    }

    public Unit create(UnitDto unitDto) {
        Unit unit = this.convertToUnit(unitDto);
        Product product = this.productService.findByIdOrThrowError(unitDto.getProductId());

        boolean invalidVariableUnit =
                product.isVariable()
                && unitDto.getToBaseUnit() == 0;

        if (invalidVariableUnit) {
            throw new InvalidVariableUnit();
        }

        return this.unitRepository.save(unit);
    }

    public Unit update(String id, UnitDto unitDto) throws RuntimeException {
        Unit unit = findByIdOrThrowError(id);
        Product product = this.productService.findByIdOrThrowError(unitDto.getProductId());

        boolean invalidVariableUnit =
                product.isVariable()
                        && unitDto.getToBaseUnit() == 0;

        if (invalidVariableUnit) {
            throw new InvalidVariableUnit();
        }

        unit.setName(unitDto.getName());
        unit.setQuantity(unitDto.getQuantity());
        unit.setPrice(unitDto.getPrice());
        unit.setSku(unitDto.getSku());
        unit.setToBaseUnit(unit.getToBaseUnit());
        unit.setProduct(product);

        return this.unitRepository.save(unit);
    }

    public Unit delete(String id) throws RuntimeException {
        Unit unit = this.findByIdOrThrowError(id);

        this.unitRepository.delete(unit);
        return unit;
    }

    public UnitDto convertToUnitDto(Unit unit) {
        return modelMapper.map(unit, UnitDto.class);
    }

    public List<UnitDto> convertToUnitDto(List<Unit> units) {
        return units
                .stream()
                .map((unit) -> modelMapper.map(unit, UnitDto.class))
                .collect(Collectors.toList());
    }

    public Unit convertToUnit(UnitDto unitDto) {
        return modelMapper.map(unitDto, Unit.class);
    }
}
