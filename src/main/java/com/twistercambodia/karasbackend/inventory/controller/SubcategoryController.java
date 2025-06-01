package com.twistercambodia.karasbackend.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.service.SubcategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("subcategories")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    private final AuditService auditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public SubcategoryController(
            SubcategoryService subcategoryService,
            AuditService auditService,
            ObjectMapper objectMapper
            ) {
        this.subcategoryService = subcategoryService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<SubcategoryResponseDto> getAllSubcategory(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "categoryId", required = false) String categoryId
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findAll(q, categoryId)
        );
    }

    @GetMapping("{id}")
    public SubcategoryResponseDto getSubcategoryById(
            @PathVariable("id") String id
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public SubcategoryResponseDto createSubcategory(
            @RequestPart(value = "data", required = true) SubcategoryRequestDto subcategoryRequestDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Subcategory subcategory = this.subcategoryService.create(subcategoryRequestDto, file);
        this.logger.info("Creating subcategory={}", subcategory);

        SubcategoryResponseDto subcategoryResponseDto =
                this.subcategoryService.convertToSubcategoryDto(subcategory);

        // create audit log of Category Creation
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(subcategoryResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Subcategory Creation");
        auditDTO.setRequestUrl("/subcategories");
        auditDTO.setService(ServiceEnum.SUBCATEGORY);
        auditDTO.setHttpMethod(HttpMethod.POST);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for subcategory={}", audit);

        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @PutMapping("{id}")
    public SubcategoryResponseDto updateSubcategory(
            @RequestPart(value = "data", required = true) SubcategoryRequestDto subcategoryRequestDto,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws RuntimeException, IOException {
        Subcategory oldSubcategory = this.subcategoryService.findByIdOrThrowError(id);
        Subcategory subcategory = this.subcategoryService.update(id, subcategoryRequestDto, file);
        this.logger.info("Updating subcategory={}", subcategory);

        SubcategoryResponseDto oldSubcategoryDto = this.subcategoryService.convertToSubcategoryDto(oldsubcategory);
        SubcategoryResponseDto subcategoryResponseDto =
                this.subcategoryService.convertToSubcategoryDto(subcategory);

        // create audit log of Category Update
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldSubcategoryDto);
        String newValueJSON = objectMapper.writeValueAsString(subcategoryResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Subcategory Update");
        auditDTO.setRequestUrl("/subcategories/" + id);
        auditDTO.setService(ServiceEnum.SUBCATEGORY);
        auditDTO.setHttpMethod(HttpMethod.PUT);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for subcategory={}", audit);

        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @DeleteMapping("{id}")
    public SubcategoryResponseDto deleteSubcategory(
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws RuntimeException, IOException {
        Subcategory oldSubcategory = this.subcategoryService.findByIdOrThrowError(id);
        Subcategory subcategory = this.subcategoryService.delete(id);
        this.logger.info("Deleting subcategory={}", subcategory);

        SubcategoryResponseDto oldSubcategoryDTO = this.subcategoryService.convertToSubcategoryDto(oldSubcategory);
        SubcategoryResponseDto subcategoryResponseDto =
                this.subcategoryService.convertToSubcategoryDto(subcategory);

        // create audit log of Category Deletion
        AuditDTO auditDTO = new AuditDTO();

        String oldValueJSON = objectMapper.writeValueAsString(oldSubcategoryDTO);
        String newValueJSON = objectMapper.writeValueAsString(subcategoryResponseDto);

        auditDTO.setOldValue(oldValueJSON);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Subcategory Deletion");
        auditDTO.setRequestUrl("/subcategories/" + id);
        auditDTO.setService(ServiceEnum.SUBCATEGORY);
        auditDTO.setHttpMethod(HttpMethod.DELETE);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for subcategory={}", audit);

        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }
}
