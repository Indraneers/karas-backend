package com.twistercambodia.karasbackend.sale.repository;

import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s")
    List<Sale> findAll();
}
