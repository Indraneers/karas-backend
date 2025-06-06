package com.twistercambodia.karasbackend.audit.controller;

import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("audits")
public class AuditController {
    private final AuditService auditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("{id}")
    public AuditDTO getAuditById(
            @PathVariable("id") String id
    ) throws IOException {
        Audit audit = this.auditService.findById(id);
        return auditService.convertToAuditDTO(audit);
    }

    @GetMapping("audit-service/{service}")
    public Page<AuditDTO> getAuditsByService(
            @RequestParam() int page,
            @PathVariable("service")ServiceEnum serviceEnum
            ) {
        Page<Audit> audits = this.auditService.findByService(serviceEnum, page);
        return auditService.convertToAuditDTO(audits);
    }
}
