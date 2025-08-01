package com.twistercambodia.karasbackend.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.auth.service.UserService;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.service.CategoryService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public CategoryController(
            CategoryService categoryService
    , AuditService auditService
    , ObjectMapper objectMapper) {
        this.categoryService = categoryService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<CategoryDto> getAllCategory(
            @RequestParam(value = "q", required = false) String q
    ) {
        return this.categoryService.convertToCategoryDto(
                this.categoryService.findAll(q)
        );
    }

    @GetMapping("{id}")
    public CategoryDto getCategoryById(
            @PathVariable("id") String id
    ) {
        return this.categoryService.convertToCategoryDto(
                this.categoryService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public CategoryDto createCategory(
            @RequestPart(value = "data", required = true) CategoryDto categoryDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal(errorOnInvalidType=true) Jwt jwt
    ) throws IOException {
        Category category = this.categoryService.create(categoryDto, file);
        this.logger.info("Creating category={}", category);

        CategoryDto categoryDTO = this.categoryService.convertToCategoryDto(category);

        // create audit log of Category Creation
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(categoryDTO);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Category Creation");
        auditDTO.setResourceName(category.getName());
        auditDTO.setRequestUrl("/categories");
        auditDTO.setService(ServiceEnum.CATEGORY);
        auditDTO.setHttpMethod(HttpMethod.POST);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for category={}", audit);

        return categoryDTO;
    }

    @PutMapping("{id}")
    public CategoryDto updateCategory(
            @RequestPart(value = "data", required = true) CategoryDto categoryDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Category oldCategory = this.categoryService.findByIdOrThrowError(id);
        CategoryDto oldCategoryDTO = this.categoryService.convertToCategoryDto(oldCategory);

        Category category = this.categoryService.update(id, categoryDto, file);
        this.logger.info("Updating category={}", category);

        CategoryDto categoryDTO = this.categoryService.convertToCategoryDto(category);

        // create audit log of Category Creation
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldCategoryDTO);
        String newValueJSON = objectMapper.writeValueAsString(categoryDTO);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Category Update");
        auditDTO.setResourceName(category.getName());
        auditDTO.setRequestUrl("/categories/" + id);
        auditDTO.setService(ServiceEnum.CATEGORY);
        auditDTO.setHttpMethod(HttpMethod.PUT);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for category={}", audit);

        return this.categoryService.convertToCategoryDto(category);
    }

    @DeleteMapping("{id}")
    public CategoryDto deleteCategory(
            @PathVariable("id") String id,
            @AuthenticationPrincipal Jwt jwt
    ) throws RuntimeException, IOException {
        Category oldCategory = this.categoryService.findByIdOrThrowError(id);
        CategoryDto oldCategoryDTO = this.categoryService.convertToCategoryDto(oldCategory);

        Category category = this.categoryService.delete(id);
        this.logger.info("Deleting category={}", category);

        CategoryDto categoryDTO = this.categoryService.convertToCategoryDto(category);

        // create audit log of Category Creation
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldCategoryDTO);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(null);

        auditDTO.setName("Category Deletion");
        auditDTO.setResourceName(category.getName());
        auditDTO.setRequestUrl("/categories/" + id);
        auditDTO.setService(ServiceEnum.CATEGORY);
        auditDTO.setHttpMethod(HttpMethod.DELETE);

        User user = new User();
        user.setId(jwt.getSubject());

        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for category={}", audit);

        return this.categoryService.convertToCategoryDto(category);
    }
}
