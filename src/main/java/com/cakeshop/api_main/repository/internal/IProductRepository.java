package com.cakeshop.api_main.repository.internal;

import com.cakeshop.api_main.dto.response.product.ProductReviewResponse;
import com.cakeshop.api_main.model.Discount;
import com.cakeshop.api_main.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    boolean existsByCategoryId(String categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount")
    void updateDiscount(@Param("discount") Discount discount);

    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount WHERE p.category.id = :categoryId")
    void updateDiscount(@Param("discount") Discount discount, @Param("categoryId") String categoryId);

    @Query(
            value = """
                    SELECT * FROM tbl_product 
                    WHERE MATCH(name_unaccent) AGAINST(:keyword IN BOOLEAN MODE)
                    AND status = :status
                    ORDER BY MATCH(name_unaccent) AGAINST(:keyword IN BOOLEAN MODE) DESC
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM tbl_product 
                    WHERE MATCH(name_unaccent) AGAINST(:keyword IN BOOLEAN MODE)
                    AND status = :status
                    """,
            nativeQuery = true
    )
    Page<Product> searchUnaccented(
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            Pageable pageable
    );

    @Query(value = """
                SELECT COUNT(*) FROM tbl_product 
                WHERE MATCH(name_unaccent) AGAINST(:keyword IN BOOLEAN MODE)
                AND status = :status
            """, nativeQuery = true)
    long countSearchByNameFullText(
            @Param("keyword") String keyword,
            @Param("status") Integer status
    );
}
