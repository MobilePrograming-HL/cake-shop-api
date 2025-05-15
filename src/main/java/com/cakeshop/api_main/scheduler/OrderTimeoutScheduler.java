package com.cakeshop.api_main.scheduler;

import com.cakeshop.api_main.constant.BaseConstant;
import com.cakeshop.api_main.model.Account;
import com.cakeshop.api_main.model.Order;
import com.cakeshop.api_main.model.OrderStatus;
import com.cakeshop.api_main.repository.internal.IAccountRepository;
import com.cakeshop.api_main.repository.internal.IOrderRepository;
import com.cakeshop.api_main.service.redis.IRedisService;
import com.cakeshop.api_main.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OrderTimeoutScheduler {
    private final long intervalTime = 5 * 60 * 1000; // 5 minutes

    @Autowired
    private IOrderRepository orderRepository;

    @Scheduled(fixedRate = intervalTime)
    @Transactional
    public void cancelExpiredOrders() {
        log.info("Starting cleanup of expired orders");
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(5);
        List<Order> expiredOrders = orderRepository.findPendingOrdersBefore(timeoutThreshold, BaseConstant.ORDER_STATUS_PENDING);

        for (Order order : expiredOrders) {
            OrderStatus cancelStatus = new OrderStatus();
            cancelStatus.setStatus(BaseConstant.ORDER_STATUS_CANCELED);
            cancelStatus.setDate(new Date());
            cancelStatus.setOrder(order);

            order.setCurrentStatus(cancelStatus);
            order.getOrderStatuses().add(cancelStatus);
        }

        orderRepository.saveAll(expiredOrders);

        if (!expiredOrders.isEmpty()) {
            log.info("❌ Đã huỷ {} đơn hàng quá hạn thanh toán", expiredOrders.size());
        }
    }
}
