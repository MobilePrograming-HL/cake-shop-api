package com.cakeshop.api_main.repository.internal;

import com.cakeshop.api_main.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByIdAndCustomerAccountUsername(String orderId, String username);

    Optional<Order> findByCode(String code);


    boolean existsByAddressId(String addressId);

    @Query("SELECT o FROM Order o WHERE o.currentStatus.status = :status AND o.createdAt <= :timeout")
    List<Order> findPendingOrdersBefore(@Param("timeout") LocalDateTime timeout,
                                        @Param("status") Integer status);
}
