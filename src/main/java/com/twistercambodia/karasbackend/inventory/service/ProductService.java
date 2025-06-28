package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableProduct;

import com.twistercambodia.karasbackend.inventory.repository.ProductRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final StorageService storageService;
    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;
    private final ModelMapper modelMapper;

    public ProductService(
            ProductRepository productRepository,
            SubcategoryService subcategoryService,
            ModelMapper modelMapper,
            StorageService storageService
    ) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.subcategoryService = subcategoryService;
        this.storageService = storageService;
    }

    public Page<Product> findAll(String query, String subcategoryId, int page) {
        if (Objects.equals(query, "")) {
            return this.productRepository.findAll(null, subcategoryId, PageRequest.of(page, 10));
        }
        return this.productRepository.findAll(query, subcategoryId, PageRequest.of(page, 10));
    }

    public Product findByIdOrThrowError(String id) throws RuntimeException {
        return this.productRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found with ID=" + id));
    }

    @Transactional
    public Product create(ProductRequestDto productRequestDto, MultipartFile image) throws IOException {
        Product product = this.convertToProduct(productRequestDto);
        boolean invalidVariableProduct =
                productRequestDto.isVariable()
                && productRequestDto.getBaseUnit().isEmpty();

        if (invalidVariableProduct) {
            throw new InvalidVariableProduct();
        }

        product = this.productRepository.save(product);

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            product.setImg(uploadProductImg(product.getId(), ext, image.getInputStream()));
            product = this.productRepository.save(product);
        }
        return product;
    }

    @Transactional
    public Product update(String id, ProductRequestDto productRequestDto, MultipartFile image) throws RuntimeException, IOException {
        Product product = findByIdOrThrowError(id);
        Subcategory subcategory = subcategoryService.findByIdOrThrowError(productRequestDto.getSubcategoryId());

        boolean invalidVariableProduct =
                productRequestDto.isVariable()
                        && productRequestDto.getBaseUnit().isEmpty();

        if (invalidVariableProduct) {
            throw new InvalidVariableProduct();
        }

        product.setName(productRequestDto.getName());
        product.setSubcategory(subcategory);
        product.setVariable(productRequestDto.isVariable());
        product.setBaseUnit(productRequestDto.getBaseUnit());
        product.setIdentifier(productRequestDto.getIdentifier());

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            product.setImg(uploadProductImg(product.getId(), ext, image.getInputStream()));
        }

        return this.productRepository.save(product);
    }

    public Product delete(String id) throws RuntimeException {
        Product product = this.findByIdOrThrowError(id);
        if (product.getImg() != null && !product.getImg().isEmpty()) {
            deleteProductImg(product.getImg());
        }
        this.productRepository.delete(product);
        return product;
    }

    public ProductResponseDto convertToProductDto(Product product) {
        ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
        if (!product.getImg().isEmpty()) {
            productResponseDto.setImg(
                    storageService.generatePresignedUrl(
                            product.getImg(),
                            Duration.ofHours(1)
                    )
            );
        }
        return productResponseDto;
    }

    public List<ProductResponseDto> convertToProductDto(List<Product> products) {
        return products
                .stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList());
    }

    public Product convertToProduct(ProductRequestDto productRequestDto) {
        return modelMapper.map(productRequestDto, Product.class);
    }

    public String getProductImgUrl(String objectName, String ext) {
        return "/products/" + objectName + '.' + ext;
    }

    public String uploadProductImg(String id, String ext, InputStream inputStream) {
        String filename = getProductImgUrl(id, ext);
        storageService.uploadFile(
                filename,
                inputStream
        );
        return filename;
    }

    public void deleteProductImg(String filename) {
        storageService.deleteFile(
                filename
        );
    }
}