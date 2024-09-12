package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.ProductDto;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return this.productService.convertToProductDto(
                this.productService.findAll()
        );
    }

    @GetMapping("{id}")
    public ProductDto getProductById(
            @PathVariable("id") String id
    ) {
        return this.productService.convertToProductDto(
                this.productService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public ProductDto createProduct(
            @RequestBody ProductDto productDto
    ) {
        Product product = this.productService.create(productDto);
        this.logger.info("Creating product={}", product);
        return this.productService.convertToProductDto(product);
    }

    @PutMapping("{id}")
    public ProductDto updateProduct(
            @RequestBody ProductDto productDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Product product = this.productService.update(id, productDto);
        this.logger.info("Updating product={}", product);
        return this.productService.convertToProductDto(product);
    }

    @DeleteMapping("{id}")
    public ProductDto deleteProduct(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Product product = this.productService.delete(id);
        this.logger.info("Deleting product={}", product);
        return this.productService.convertToProductDto(product);
    }
}
