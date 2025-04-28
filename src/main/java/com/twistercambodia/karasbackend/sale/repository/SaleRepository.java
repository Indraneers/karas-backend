package com.twistercambodia.karasbackend.sale.repository;

import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {
    Page<Sale> findAll(Specification<Sale> specification, Pageable pageable);
}
