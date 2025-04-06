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
            product.setImg(uploadProductImg(product.getId(), image.getInputStream()));
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
            product.setImg(uploadProductImg(product.getId(), image.getInputStream()));
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
        return modelMapper.map(product, ProductResponseDto.class);
    }

    public List<ProductResponseDto> convertToProductDto(List<Product> products) {
        return products
                .stream()
                .map((product) -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public Product convertToProduct(ProductRequestDto productRequestDto) {
        return modelMapper.map(productRequestDto, Product.class);
    }

    public String getProductImg(String id, String ext) {
        return "/products/" + id + "-" + System.currentTimeMillis() + "." + ext;
    }

    public String uploadProductImg(String id, InputStream inputStream) {
        String filename = getProductImg(id, "png");
        storageService.uploadFile(
                filename,
                inputStream,
                "image/png"
        );
        return filename;
    }

    public void deleteProductImg(String filename) {
        storageService.deleteFile(
                filename
        );
    }
}