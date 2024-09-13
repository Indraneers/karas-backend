package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    public UnitService(UnitRepository unitRepository, ModelMapper modelMapper) {
        this.unitRepository = unitRepository;
        this.modelMapper = modelMapper;
    }

    public List<Unit> findAll() {
        return this.unitRepository.findAll();
    }

    public Unit findByIdOrThrowError(String id) throws RuntimeException {
        return this.unitRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Unit Not Found with ID=" + id));
    }

    public Unit create(UnitDto unitDto) {
        Unit unit = this.convertToUnit(unitDto);
        return this.unitRepository.save(unit);
    }

    public Unit update(String id, UnitDto unitDto) throws RuntimeException {
        Unit unit = findByIdOrThrowError(id);

        unit.setName(unitDto.getName());
        unit.setQuantity(unitDto.getQuantity());
        unit.setPrice(unitDto.getPrice());
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
