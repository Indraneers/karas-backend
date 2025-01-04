package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.exception.InvalidVariableProduct;

import com.twistercambodia.karasbackend.inventory.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, SubcategoryService subcategoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.subcategoryService = subcategoryService;
    }

    public List<Product> findAll(String query, String subcategoryId) {
        return this.productRepository.findAll(query, subcategoryId);
    }

    public Product findByIdOrThrowError(String id) throws RuntimeException {
        return this.productRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found with ID=" + id));
    }

    public Product create(ProductRequestDto productRequestDto) {
        Product product = this.convertToProduct(productRequestDto);

        boolean invalidVariableProduct =
                productRequestDto.isVariable()
                && productRequestDto.getBaseUnit().isEmpty();

        if (invalidVariableProduct) {
            throw new InvalidVariableProduct();
        }

        return this.productRepository.save(product);
    }

    public Product update(String id, ProductRequestDto productRequestDto) throws RuntimeException {
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
        product.setVariable(product.isVariable());
        product.setBaseUnit(productRequestDto.getBaseUnit());

        return this.productRepository.save(product);
    }

    public Product delete(String id) throws RuntimeException {
        Product product = this.findByIdOrThrowError(id);

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
}