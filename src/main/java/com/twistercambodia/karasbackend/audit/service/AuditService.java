package com.twistercambodia.karasbackend.audit.service;

import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.repository.AuditRepository;
import com.twistercambodia.karasbackend.utility.GZIPCompression;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
public class AuditService {
    private final AuditRepository auditRepository;
    private final ModelMapper modelMapper;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
        this.modelMapper = new ModelMapper();
    }

    public Page<Audit> findByService(ServiceEnum service, int page) {
        return auditRepository.findAuditsByService(
                service,
                PageRequest.of(page, 10, Sort.by("timestamp").descending()))
                .map((a) -> {
                    try {
                        return decompressAudit(a);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Audit findById(String id) throws IOException {
        Audit audit = auditRepository.findById(id).orElseThrow(() -> new RuntimeException("Audit Not Found with ID=" + id));
        return decompressAudit(audit);
    }

    public Audit create(AuditDTO auditDTO) throws IOException {
        Audit audit = new Audit();

        audit.setTimestamp(LocalDateTime.now());
        audit.setHttpMethod(auditDTO.getHttpMethod());
        audit.setName(auditDTO.getName());
        audit.setResourceName(auditDTO.getResourceName());
        audit.setRequestUrl(auditDTO.getRequestUrl());
        audit.setService(auditDTO.getService());
        audit.setUser(auditDTO.getUser());
        audit.setOldValue(auditDTO.getOldValue());
        audit.setNewValue(auditDTO.getNewValue());

        return this.auditRepository.save(compressAudit(audit));
    }

    public Audit decompressAudit(Audit audit) throws IOException {
        if (audit.getNewValue() != null) {
            audit.setNewValue(GZIPCompression.decompress(audit.getNewValue()));
        }
        if (audit.getOldValue() != null) {
            audit.setOldValue(GZIPCompression.decompress(audit.getOldValue()));
        }

        return audit;
    }

    public Audit compressAudit(Audit audit) throws IOException {
        if (audit.getNewValue() != null) {
            audit.setNewValue(GZIPCompression.compress(audit.getNewValue()));
        }
        if (audit.getOldValue() != null) {
            audit.setOldValue(GZIPCompression.compress(audit.getOldValue()));
        }

        return audit;
    }

    public Page<AuditDTO> convertToAuditDTO(Page<Audit> audits) {
        return audits.map(this::convertToAuditDTO);
    }

    public AuditDTO convertToAuditDTO(Audit audit) {
        return new AuditDTO(audit);
    }
}
