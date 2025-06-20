package com.twistercambodia.karasbackend.sale.specification;

import com.twistercambodia.karasbackend.sale.dto.SaleFilter;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SaleSpecification {
    public static final String CREATEDAT = "createdAt";
    public static final String CUSTOMER = "customer";
    public static final String USER = "user";
    public static final String ID = "id";

    private SaleSpecification() {}

    public static Specification<Sale> filterBy(SaleFilter saleFilter) {
        System.out.println("USER ID:" + saleFilter.getUserId());
        return Specification
                .where(hasCreatedAtGreaterThan(saleFilter.getCreatedAtFrom()))
                .and(hasCreatedAtLessThan(saleFilter.getCreatedAtTo()))
                .and(hasCustomerId(saleFilter.getCustomerId()))
                .and(hasUserId(saleFilter.getUserId()));
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

    private static Specification<Sale> hasCustomerId(String customerId) {
        return (root, query, cb) -> customerId == null ?
                cb.conjunction()
                :
                cb.equal(root.get(CUSTOMER).get(ID), customerId);
    }

    private static Specification<Sale> hasUserId(String userId) {
        return (root, query, cb) -> userId == null ?
                cb.conjunction()
                :
                cb.equal(root.get(USER).get(ID), userId);
    }
}
