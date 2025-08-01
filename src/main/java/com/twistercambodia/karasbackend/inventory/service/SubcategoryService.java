package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.repository.SubcategoryRepository;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    public SubcategoryService
            (
                    SubcategoryRepository subcategoryRepository,
                    CategoryService categoryService,
                    ModelMapper modelMapper,
                    StorageService storageService
            ) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    public List<Subcategory> findAll(String query, String categoryId) {
        return this.subcategoryRepository.findAll(query, categoryId);
    }

    public Subcategory findByIdOrThrowError(String id) throws RuntimeException {
        return this.subcategoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategory Not Found with ID=" + id));
    }

    @Transactional
    public Subcategory create(SubcategoryRequestDto subcategoryRequestDto, MultipartFile image) throws IOException {
        Subcategory subcategory = this.convertToSubcategory(subcategoryRequestDto);
        subcategory = this.subcategoryRepository.save(subcategory);
        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            if (ext == "svg") {
                subcategory.setImg(uploadSubcategoryIcon(subcategory.getId(), image.getInputStream()));
                subcategory = this.subcategoryRepository.save(subcategory);
            }
        }

        return subcategory;
    }

    @Transactional
    public Subcategory update(String id, SubcategoryRequestDto subcategoryRequestDto, MultipartFile image) throws RuntimeException, IOException {
        Subcategory subcategory = findByIdOrThrowError(id);
        Category category = categoryService.findByIdOrThrowError(subcategoryRequestDto.getCategoryId());

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            if (ext.equals("svg")) {
                subcategory.setImg(uploadSubcategoryIcon(subcategory.getId(), image.getInputStream()));
            }
        }

        subcategory.setName(subcategoryRequestDto.getName());
        subcategory.setCategory(category);
        subcategory.setColor(subcategoryRequestDto.getColor());
        subcategory.setUpdatedAt(Instant.now());
        return this.subcategoryRepository.save(subcategory);
    }

    @Transactional
    public Subcategory delete(String id) throws RuntimeException {
        Subcategory subcategory = this.findByIdOrThrowError(id);
        if (subcategory.getImg() != null && !subcategory.getImg().isEmpty()) {
            deleteSubcategoryIcon(subcategory.getImg());
        }
        this.subcategoryRepository.delete(subcategory);
        return subcategory;
    }

    public SubcategoryResponseDto convertToSubcategoryDto(Subcategory subcategory) {
        SubcategoryResponseDto subcategoryResponseDto =  new SubcategoryResponseDto(subcategory);
        if (subcategory.getImg() != null && !subcategory.getImg().isEmpty()) {
            subcategoryResponseDto.setImg(
                    storageService.generatePresignedUrl(subcategory.getImg(), Duration.ofHours(1)));
        }
        return subcategoryResponseDto;
    }

    public List<SubcategoryResponseDto> convertToSubcategoryDto(List<Subcategory> subcategories) {
        return subcategories
                .stream()
                .map(this::convertToSubcategoryDto)
                .collect(Collectors.toList());
    }

    public Subcategory convertToSubcategory(SubcategoryRequestDto subcategoryRequestDto) {
        return modelMapper.map(subcategoryRequestDto, Subcategory.class);
    }

    public String getSubcategoryIcon(String id) {
        return "/subcategories/" + id + ".svg";
    }

    public String uploadSubcategoryIcon(String id, InputStream inputStream) {
        String filename = getSubcategoryIcon(id);
        storageService.uploadFile(
                filename,
                inputStream
        );
        return filename;
    }

    public void deleteSubcategoryIcon(String filename) {
        storageService.deleteFile(
                filename
        );
    }
}
