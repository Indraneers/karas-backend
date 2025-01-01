package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.ProductDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
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
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryService  = categoryService;
    }

    public List<Product> findAll(String query, String categoryId) {
        return this.productRepository.findAll(query, categoryId);
    }

    public Product findByIdOrThrowError(String id) throws RuntimeException {
        return this.productRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found with ID=" + id));
    }

    public Product create(ProductDto productDto) {
        Product product = this.convertToProduct(productDto);

        boolean invalidVariableProduct =
                productDto.isVariable()
                && productDto.getBaseUnit().isEmpty();

        if (invalidVariableProduct) {
            throw new InvalidVariableProduct();
        }

        return this.productRepository.save(product);
    }

    public Product update(String id, ProductDto productDto) throws RuntimeException {
        Product product = findByIdOrThrowError(id);
        Category category = categoryService.findByIdOrThrowError(productDto.getCategoryId());

        boolean invalidVariableProduct =
                productDto.isVariable()
                        && productDto.getBaseUnit().isEmpty();

        if (invalidVariableProduct) {
            throw new InvalidVariableProduct();
        }

        product.setName(productDto.getName());
        product.setCategory(category);
        product.setVariable(product.isVariable());
        product.setBaseUnit(productDto.getBaseUnit());

        return this.productRepository.save(product);
    }

    public Product delete(String id) throws RuntimeException {
        Product product = this.findByIdOrThrowError(id);

        this.productRepository.delete(product);
        return product;
    }

    public ProductDto convertToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> convertToProductDto(List<Product> products) {
        return products
                .stream()
                .map((product) -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public Product convertToProduct(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }
}