package com.twistercambodia.karasbackend.sale.repository;

import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s order by s.id desc")
    Page<Sale> findAll(Pageable pageable);

    Page<Sale> findAllByCustomerId(String customerId, Pageable pageable);
}
