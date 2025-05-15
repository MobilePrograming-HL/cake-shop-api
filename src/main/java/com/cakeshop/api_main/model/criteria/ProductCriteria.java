package com.cakeshop.api_main.model.criteria;

import com.cakeshop.api_main.constant.BaseConstant;
import com.cakeshop.api_main.model.Discount;
import com.cakeshop.api_main.model.Product;
import com.cakeshop.api_main.model.criteria.base.BaseCriteria;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductCriteria extends BaseCriteria<Product> {
    private String name;
    private Integer status;
    private String categoryId;
    private String priceSort;
    private String soldSort;
    private String createdSort;
    @Min(value = 0)
    private Double fromPrice;
    @Min(value = 0)
    private Double toPrice;

    @Override
    public Specification<Product> getSpecification() {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }
            if (getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), getStatus()));
            }
            if (StringUtils.hasText(getCategoryId())) {
                predicates.add(cb.equal(root.get("category").get("id"), getCategoryId()));
            }
            if (getFromPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), getFromPrice()));
            }
            if (getToPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), getToPrice()));
            }
            if (StringUtils.hasText(getPriceSort())) {
                Join<Product, Discount> discountJoin = root.join("discount", JoinType.LEFT);
                Path<Integer> discountRaw = discountJoin.get("discountPercentage");

                Expression<Integer> discountInverse = cb.diff(
                        cb.literal(100),
                        discountRaw
                );

                Expression<Double> discountFactor = cb.prod(
                        cb.toDouble(discountInverse),
                        cb.literal(0.01)
                );

                Expression<Double> discountMultiplier = cb.<Double>selectCase()
                        .when(cb.isNotNull(discountRaw), discountFactor)
                        .otherwise(cb.literal(1.0));

                Expression<Double> priceAfterDiscount = cb.prod(
                        root.get("price"),
                        discountMultiplier
                );

                if (getPriceSort().equalsIgnoreCase(BaseConstant.SORT_ASC)) {
                    query.orderBy(cb.asc(priceAfterDiscount));
                } else if (getPriceSort().equalsIgnoreCase(BaseConstant.SORT_DESC)) {
                    query.orderBy(cb.desc(priceAfterDiscount));
                }
            }
            if (StringUtils.hasText(getCreatedSort())) {
                if (getCreatedSort().equalsIgnoreCase(BaseConstant.SORT_ASC)) {
                    query.orderBy(cb.asc(root.get("createdAt")));
                } else if (getCreatedSort().equalsIgnoreCase(BaseConstant.SORT_DESC)) {
                    query.orderBy(cb.desc(root.get("createdAt")));
                }
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
