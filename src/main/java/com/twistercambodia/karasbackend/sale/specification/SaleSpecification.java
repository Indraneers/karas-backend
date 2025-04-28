package com.twistercambodia.karasbackend.sale.specification;

import com.twistercambodia.karasbackend.sale.dto.SaleFilter;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SaleSpecification {
    public static final String CREATEDAT = "createdAt";

    private SaleSpecification() {}

    public static Specification<Sale> filterBy(SaleFilter saleFilter) {
        return Specification
                .where(hasCreatedAtGreaterThan(saleFilter.getCreatedAtFrom()))
                .and(hasCreatedAtLessThan(saleFilter.getCreatedAtTo()));
    }

    private static Specification<Sale> hasCreatedAtGreaterThan(LocalDateTime createdAtFrom) {
        return (root, query, cb) -> createdAtFrom == null ?
                cb.conjunction()
                :
                cb.greaterThan(root.get(CREATEDAT), createdAtFrom);
    }

    private static Specification<Sale> hasCreatedAtLessThan(LocalDateTime createdAtTo) {
        return (root, query, cb) -> createdAtTo == null ?
                cb.conjunction()
                :
                cb.lessThan(root.get(CREATEDAT), createdAtTo);
    }
}
