package com.twistercambodia.karasbackend.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.inventory.dto.ProductRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.ProductResponseDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Product;
import com.twistercambodia.karasbackend.inventory.service.ProductService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;
    private final AuditService auditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public ProductController(
            ProductService productService,
            AuditService auditService,
            ObjectMapper objectMapper
    ) {
        this.productService = productService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
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
            @RequestParam(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        Product product = this.productService.create(productRequestDto, file);
        this.logger.info("Creating product={}", product);

        ProductResponseDto productResponseDto =
                this.productService.convertToProductDto(product);

        // create audit log of Category Deletion
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(productResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Product Creation");
        auditDTO.setResourceName(product.getName());
        auditDTO.setRequestUrl("/products");
        auditDTO.setService(ServiceEnum.PRODUCT);
        auditDTO.setHttpMethod(HttpMethod.POST);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for product={}", audit);

        return productResponseDto;
    }

    @PutMapping("{id}")
    public ProductResponseDto updateProduct(
            @RequestPart(name = "data", required = true) ProductRequestDto productRequestDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Product oldProduct = this.productService.findByIdOrThrowError(id);
        Product product = this.productService.update(id, productRequestDto, file);
        this.logger.info("Updating product={}", product);

        ProductResponseDto oldProductDto = this.productService.convertToProductDto(oldProduct);
        ProductResponseDto productResponseDto =
                this.productService.convertToProductDto(product);

        // create audit log of Category Deletion
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldProductDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(null);

        auditDTO.setName("Product Update");
        auditDTO.setResourceName(product.getName());
        auditDTO.setRequestUrl("/products/" + id);
        auditDTO.setService(ServiceEnum.PRODUCT);
        auditDTO.setHttpMethod(HttpMethod.PUT);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for product={}", audit);

        return productResponseDto;
    }

    @DeleteMapping("{id}")
    public ProductResponseDto deleteProduct(
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Product oldProduct = this.productService.findByIdOrThrowError(id);
        Product product = this.productService.delete(id);
        this.logger.info("Deleting product={}", product);

        ProductResponseDto oldProductDto = this.productService.convertToProductDto(oldProduct);
        ProductResponseDto productResponseDto =
                this.productService.convertToProductDto(product);

        // create audit log of Product Deletion
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldProductDto);
        String newValueJSON = objectMapper.writeValueAsString(productResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Product Deletion");
        auditDTO.setResourceName(product.getName());
        auditDTO.setRequestUrl("/products/" + id);
        auditDTO.setService(ServiceEnum.PRODUCT);
        auditDTO.setHttpMethod(HttpMethod.DELETE);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for product={}", audit);

        return productResponseDto;
    }
}
