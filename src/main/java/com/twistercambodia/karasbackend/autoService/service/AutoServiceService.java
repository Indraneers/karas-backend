package com.twistercambodia.karasbackend.autoService.service;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import com.twistercambodia.karasbackend.autoService.exception.AutoServiceNotFoundException;
import com.twistercambodia.karasbackend.autoService.repository.AutoServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoServiceService {
    private final AutoServiceRepository autoServiceRepository;
    private final ModelMapper modelMapper;

    public AutoServiceService(AutoServiceRepository autoServiceRepository, ModelMapper modelMapper) {
        this.autoServiceRepository = autoServiceRepository;
        this.modelMapper = modelMapper;
    }

    public AutoService findByIdOrThrowError(String id) {
        return this.autoServiceRepository
                .findById(id)
                .orElseThrow(() -> new AutoServiceNotFoundException("AutoService not found with ID=" + id));
    }

    public List<AutoService> findAll() {
        return this.autoServiceRepository.findAll();
    }

    public AutoService create(AutoServiceDto autoServiceDto) {
        AutoService autoService = this.convertToAutoService(autoServiceDto);
        return this.autoServiceRepository.save(autoService);
    }

    public AutoService update(String id, AutoServiceDto autoServiceDto) {
        AutoService autoService = this.findByIdOrThrowError(id);

        autoService.setName(autoServiceDto.getName());
        autoService.setActive(autoServiceDto.isActive());
        autoService.setPrice(autoServiceDto.getPrice());

        return this.autoServiceRepository.save(autoService);
    }

    public AutoService delete(String id) {
        AutoService autoService = this.findByIdOrThrowError(id);

        this.autoServiceRepository.delete(autoService);
        return autoService;
    }

    public AutoService convertToAutoService(AutoServiceDto autoServiceDto) {
        return this.modelMapper.map(autoServiceDto, AutoService.class);
    }

    public AutoServiceDto convertToAutoServiceDto(AutoService autoService) {
        return this.modelMapper.map(autoService, AutoServiceDto.class);
    }

    public List<AutoServiceDto> convertToAutoServiceDto(List<AutoService> autoServices) {
        return autoServices
                .stream()
                .map((s) -> this.modelMapper.map(s, AutoServiceDto.class))
                .collect(Collectors.toList());
    }
}
