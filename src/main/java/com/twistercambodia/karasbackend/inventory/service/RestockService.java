package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.RestockItemRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.RestockRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.RestockResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Restock;
import com.twistercambodia.karasbackend.inventory.entity.RestockItem;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;
import com.twistercambodia.karasbackend.inventory.repository.RestockRepository;
import com.twistercambodia.karasbackend.sale.entity.Item;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestockService {
    private final RestockRepository restockRepository;
    private final ModelMapper objectMapper;
    private final UserService userService;
    private final UnitService unitService;

    public RestockService(RestockRepository restockRepository, ModelMapper objectMapper, UserService userService, UnitService unitService) {
        this.restockRepository = restockRepository;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.unitService = unitService;
    }

    public List<Restock> findAll() {
        return this.restockRepository.findAll();
    }

    public Restock findByIdOrThrowError(String id) {
        return this.restockRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Restock Not Found with ID=" + id));
    }

    @Transactional
    public Restock create(RestockRequestDto restockRequestDto) throws Exception {
        Restock restock = convertToRestock(restockRequestDto);

        User user = this.userService.findByIdOrThrowError(restockRequestDto.getUserId());
        restock.setUser(user);
        restock.getItems().clear();
        restock.setCreatedAt(LocalDateTime.parse(restockRequestDto.getCreatedAt()));

        for (RestockItemRequestDto item : restockRequestDto.getItems()) {
            RestockItem restockItem = this.objectMapper.map(item, RestockItem.class);

            Unit unit = unitService.findByIdOrThrowError(item.getUnitId());
            restockItem.setId(null);
            restockItem.setUnit(unit);

            restock.getItems().add(restockItem);
            restockItem.setRestock(restock);
        }

        unitService.batchStockUpdate(restock.getItems());

        return this.restockRepository.save(restock);
    }

    public Restock convertToRestock(RestockRequestDto restockRequestDto) {
        return objectMapper.map(restockRequestDto, Restock.class);
    }

    public List<RestockResponseDto> convertToRestockResponseDto(List<Restock> restock) {
        return restock
                .stream()
                .map(this::convertToRestockResponseDto)
                .toList();
    }

    public RestockResponseDto convertToRestockResponseDto(Restock restock) {
        return new RestockResponseDto(restock);
    }

}
