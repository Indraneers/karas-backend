package com.twistercambodia.karasbackend.audit.repository;

import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditRepository extends CrudRepository<Audit, String> {
    Page<Audit> findAuditsByService(ServiceEnum service, Pageable pageable);
}
