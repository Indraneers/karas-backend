package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.BadRequestException;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.entity.RestockItem;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.enums.StockUpdate;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableUnit;
import com.twistercambodia.karasbackend.inventory.repository.UnitRepository;
import com.twistercambodia.karasbackend.sale.entity.Item;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    public UnitService(
            UnitRepository unitRepository,
            ProductService productService,
            ModelMapper modelMapper,
            StorageService storageService) {
        this.unitRepository = unitRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    public Page<Unit> findAll(String query, String productId, int page) {
        if (Objects.equals(query, "")) {
            return this.unitRepository.findAll(null, productId, PageRequest.of(page, 10));
        }
        return this.unitRepository.findAll(query, productId, PageRequest.of(page, 10));
    }

    public Unit findByIdOrThrowError(String id) throws RuntimeException {
        return this.unitRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Unit Not Found with ID=" + id));
    }

    @Transactional
    public Unit create(UnitRequestDto unitRequestDto, MultipartFile image) throws IOException {
        Unit unit = this.convertToUnit(unitRequestDto);
        Product product = this.productService.findByIdOrThrowError(unitRequestDto.getProductId());

        boolean invalidVariableUnit =
                product.isVariable()
                && unitRequestDto.getToBaseUnit() == 0;

        if (invalidVariableUnit) {
            throw new InvalidVariableUnit();
        }

        // save unit first
        unit = this.unitRepository.save(unit);

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            unit.setImg(uploadUnitImg(unit.getId(), ext, image.getInputStream()));
            unit = this.unitRepository.save(unit);
        }

        return unit;
    }

    public Unit update(String id, UnitRequestDto unitRequestDto, MultipartFile image) throws RuntimeException, IOException {
        Unit unit = findByIdOrThrowError(id);
        Product product = this.productService.findByIdOrThrowError(unitRequestDto.getProductId());

        boolean isInvalidToBaseUnit = unitRequestDto.getToBaseUnit() == 0;
        boolean invalidVariableUnit =
                product.isVariable()
                        && isInvalidToBaseUnit;

        if (invalidVariableUnit) {
            throw new InvalidVariableUnit();
        }

        if (isInvalidToBaseUnit) {
            throw new BadRequestException("Invalid to base unit field, it must be greater than 0");
        }

        unit.setName(unitRequestDto.getName());
        unit.setQuantity(unitRequestDto.getQuantity());
        unit.setPrice(unitRequestDto.getPrice());
        unit.setToBaseUnit(unitRequestDto.getToBaseUnit());
        unit.setProduct(product);
        unit.setUpdatedAt(Instant.now());

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            String oldExt = storageService.getExtension(unit.getImg());
            if (oldExt != null && !oldExt.isEmpty() && !ext.equals(oldExt)) {
                deleteUnitIcon(unit.getImg());
            }
            unit.setImg(uploadUnitImg(unit.getId(), ext, image.getInputStream()));
        }

        return this.unitRepository.save(unit);
    }

    public Unit delete(String id) throws RuntimeException {
        Unit unit = this.findByIdOrThrowError(id);
        this.unitRepository.delete(unit);

        if (!unit.getImg().isEmpty()) {
            deleteUnitIcon(unit.getImg());
        }

        return unit;
    }

    public UnitResponseDto convertToUnitDto(Unit unit) {
        UnitResponseDto unitDto = new UnitResponseDto(unit);
        if (unitDto.getImg() != null && !unitDto.getImg().isEmpty()) {
            unitDto.setImg(
                    storageService.generatePresignedUrl(
                            unitDto.getImg(),
                            Duration.ofHours(1)
                    )
            );
        }

        if (unitDto.getSubcategoryImg() != null && !unitDto.getSubcategoryImg().isEmpty()) {
            unitDto.setSubcategoryImg(
                    storageService.generatePresignedUrl(
                            unitDto.getSubcategoryImg(),
                            Duration.ofHours(1)
                    )
            );
        }

        return unitDto;
    }

    public List<UnitResponseDto> convertToUnitDto(List<Unit> units) {
        return units
                .stream()
                .map(this::convertToUnitDto)
                .collect(Collectors.toList());
    }

    public Unit convertToUnit(UnitRequestDto unitRequestDto) {
        return modelMapper.map(unitRequestDto, Unit.class);
    }

    public String getUnitImgUrl(String objectName, String ext) {
        return "/units/" + objectName + "." + ext;
    }

    public String uploadUnitImg(String id, String ext, InputStream inputStream) {
        String filename = getUnitImgUrl(id, ext);
        storageService.uploadFile(
                filename,
                inputStream
        );
        return filename;
    }

    public void batchStockUpdate(List<Item> items, StockUpdate stockUpdate) {
        List<Unit> batchUnit = items
                .stream()
                .map((i) -> stockUpdate(i.getUnit(), i.getQuantity(), stockUpdate))
                .toList();

        this.unitRepository.saveAll(batchUnit);
    }

    public void batchStockUpdate(List<RestockItem> restockItems) {
        List<Unit> batchUnit = restockItems
                .stream()
                .map((i) -> stockUpdate(i.getUnit(), i.getQuantity(), i.getStatus()))
                .toList();

        System.out.println(batchUnit);
        this.unitRepository.saveAll(batchUnit);
    }

    public Unit stockUpdate(Unit unit, int quantity, StockUpdate stockUpdate) {
        // 1. check if stock update is restock
        if (stockUpdate == StockUpdate.RESTOCK) {
            // 2. if it is, add the quantity to the unit and update it
            unit.setQuantity(
                    unit.getQuantity() + quantity
            );
        } else {
            unit.setQuantity(
                    unit.getQuantity() - quantity
            );
        }

        return unit;
    }

    public void deleteUnitIcon(String filename) {
        storageService.deleteFile(
                filename
        );
    }
}
