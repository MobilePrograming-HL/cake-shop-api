package com.cakeshop.api_main.repository.internal;

import com.cakeshop.api_main.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, String>, JpaSpecificationExecutor<CartItem> {
    @Query("""
                SELECT ci
                FROM CartItem ci
                JOIN ci.cart c
                JOIN c.customer cu
                WHERE cu.account.username = :username
                AND ci.id IN :ids
            """)
    List<CartItem> findAllByIdsAndUsername(@Param("ids") List<String> ids, @Param("username") String username);

}
