package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Page<ProductResponseDto> getAllProducts(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "subcategoryId", required = false) String subcategoryId,
            @RequestParam(value = "page", required = true) int page
    ) {
        return this.productService.findAll(q, subcategoryId, page)
                .map(productService::convertToProductDto);
    }

    @GetMapping("{id}")
    public ProductResponseDto getProductById(
            @PathVariable("id") String id
    ) {
        return this.productService.convertToProductDto(
                this.productService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public ProductResponseDto createProduct(
            @RequestPart(name = "data", required = true) ProductRequestDto productRequestDto,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) throws IOException {
        Product product = this.productService.create(productRequestDto, file);
        this.logger.info("Creating product={}", product);
        return this.productService.convertToProductDto(product);
    }

    @PutMapping("{id}")
    public ProductResponseDto updateProduct(
            @RequestPart(name = "data", required = true) ProductRequestDto productRequestDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable("id") String id
    ) throws RuntimeException, IOException {
        Product product = this.productService.update(id, productRequestDto, file);
        this.logger.info("Updating product={}", product);
        return this.productService.convertToProductDto(product);
    }

    @DeleteMapping("{id}")
    public ProductResponseDto deleteProduct(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Product product = this.productService.delete(id);
        this.logger.info("Deleting product={}", product);
        return this.productService.convertToProductDto(product);
    }
}
