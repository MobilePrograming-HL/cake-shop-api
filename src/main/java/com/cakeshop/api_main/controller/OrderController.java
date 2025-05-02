package com.cakeshop.api_main.controller;

import com.cakeshop.api_main.constant.BaseConstant;
import com.cakeshop.api_main.dto.request.order.BuyNowOrderRequest;
import com.cakeshop.api_main.dto.request.order.CreateOrderRequest;
import com.cakeshop.api_main.dto.request.order.UpdateOrderStatusRequest;
import com.cakeshop.api_main.dto.request.orderItem.CreateOrderItemRequest;
import com.cakeshop.api_main.dto.request.orderItem.OrderItemDetails;
import com.cakeshop.api_main.dto.response.BaseResponse;
import com.cakeshop.api_main.dto.response.PaginationResponse;
import com.cakeshop.api_main.dto.response.order.OrderResponse;
import com.cakeshop.api_main.dto.response.order.PayUrlResponse;
import com.cakeshop.api_main.dto.response.orderStatus.OrderStatusResponse;
import com.cakeshop.api_main.exception.BadRequestException;
import com.cakeshop.api_main.exception.ErrorCode;
import com.cakeshop.api_main.exception.NotFoundException;
import com.cakeshop.api_main.mapper.OrderMapper;
import com.cakeshop.api_main.mapper.OrderStatusMapper;
import com.cakeshop.api_main.model.*;
import com.cakeshop.api_main.model.criteria.OrderCriteria;
import com.cakeshop.api_main.repository.internal.*;
import com.cakeshop.api_main.service.momo.CreateMomoResponse;
import com.cakeshop.api_main.service.momo.MomoService;
import com.cakeshop.api_main.utils.BaseResponseUtils;
import com.cakeshop.api_main.utils.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderController {
    ICustomerRepository customerRepository;
    IProductRepository productRepository;
    IOrderRepository orderRepository;
    ITagRepository tagRepository;
    IAddressRepository addressRepository;
    ICartItemRepository cartItemRepository;

    OrderMapper orderMapper;
    OrderStatusMapper orderStatusMapper;

    MomoService momoService;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<OrderResponse> get(@PathVariable String id) {
        String username = SecurityUtil.getCurrentUsername();
        Order order = orderRepository.findByIdAndCustomerAccountUsername(id, username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));

        return BaseResponseUtils.success(orderMapper.fromEntityToOrderResponse(order), "Get cart successfully");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<PaginationResponse<OrderResponse>> list(
            @Valid @ModelAttribute OrderCriteria criteria,
            Pageable pageable
    ) {
        String username = SecurityUtil.getCurrentUsername();
        Customer customer = customerRepository.findByAccountUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND_ERROR));
        criteria.setCustomerId(customer.getId());
        Page<Order> pageData = orderRepository.findAll(criteria.getSpecification(), pageable);
        PaginationResponse<OrderResponse> responseDto = new PaginationResponse<>(
                orderMapper.fromEntitiesToOrderResponseList(pageData.getContent()),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
        return BaseResponseUtils.success(responseDto, "Get order list successfully");
    }

    @GetMapping(value = "/list-order-status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<List<OrderStatusResponse>> listOrderStatus(@PathVariable String id) {
        String username = SecurityUtil.getCurrentUsername();
        Order order = orderRepository.findByIdAndCustomerAccountUsername(id, username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));
        List<OrderStatus> orderStatuses = order.getOrderStatuses();
        return BaseResponseUtils.success(orderStatusMapper.fromEntitiesToOrderStatusResponseList(orderStatuses), "Get order status list successfully");
    }

    @PostMapping(value = "/buy-now", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<PayUrlResponse> buyNow(@Valid @RequestBody BuyNowOrderRequest request) {
        String username = SecurityUtil.getCurrentUsername();
        Customer customer = customerRepository.findByAccountUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND_ERROR));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_ERROR));
        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.TAG_NOT_FOUND_ERROR));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND_ERROR));
        List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
        orderItemDetailsList.add(new OrderItemDetails(product, tag, request.getQuantity()));
        Order order = new Order(customer, request.getShippingFee(), request.getPaymentMethod(), address, request.getNote());
        int status;
        if (Objects.equals(request.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO)) {
            status = BaseConstant.ORDER_STATUS_PENDING;
        } else {
            status = BaseConstant.ORDER_STATUS_PROCESSING;
        }
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(status);
        orderStatus.setDate(new Date());
        orderStatus.setOrder(order);
        order.makeOrder(orderItemDetailsList, orderStatus);
        orderRepository.save(order);

        if (Objects.equals(order.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO)) {
            try {
                Long amount = Math.round(order.getTotalAmount());
                CreateMomoResponse response = momoService.createPaymentUrl(amount, order.getId());
                PayUrlResponse payUrlResponse = new PayUrlResponse();
                payUrlResponse.setPayUrl(response.getPayUrl());
                return BaseResponseUtils.success(payUrlResponse, "Create MoMo payment URL successfully");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException(ErrorCode.CREATE_PAYMENT_ERROR);
            }
        }
        return BaseResponseUtils.success(null, "Create order successfully");
    }

//    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    public BaseResponse<Void> create(@Valid @RequestBody CreateOrderRequest request) {
//        String username = SecurityUtil.getCurrentUsername();
//        Customer customer = customerRepository.findByAccountUsername(username)
//                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND_ERROR));
//        Address address = addressRepository.findById(request.getAddressId())
//                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND_ERROR));
//
//        List<CartItem> cartItems = cartItemRepository.findAllByIdsAndUsername(request.getCartItemIds(), username);
//        if (!cartItems.isEmpty()) {
//            List<OrderItemDetails> orderItemDetailsList = cartItems.stream()
//                    .map(
//                            item -> new OrderItemDetails(item.getProduct(), item.getTag(), item.getQuantity())
//                    ).toList();
//            Order order = new Order(customer, request.getShippingFee(), request.getPaymentMethod(), address, request.getNote());
//            order.makeOrder(orderItemDetailsList);
//            orderRepository.save(order);
//            cartItemRepository.deleteAll(cartItems);
//        }
//        return BaseResponseUtils.success(null, "Create order successfully");
//    }

    @PutMapping(value = "/update-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Void> updateStatus(
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        String username = SecurityUtil.getCurrentUsername();
        Order order = orderRepository.findByIdAndCustomerAccountUsername(request.getId(), username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));
        if (order.getCurrentStatus().getStatus().equals(request.getStatus() - 1) || request.getStatus().equals(BaseConstant.ORDER_STATUS_CANCELED)) {
            OrderStatus orderStatus = new OrderStatus(request.getStatus(), new Date(), order);
            order.setCurrentStatus(orderStatus);
            order.getOrderStatuses().add(orderStatus);
        } else {
            throw new BadRequestException(ErrorCode.ORDER_STATUS_INVALID_ERROR);
        }
        orderRepository.save(order);
        return BaseResponseUtils.success(null, "Update order status successfully");
    }

    @PutMapping(value = "/cancel/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Void> cancelOrder(
            @PathVariable String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));
        order.cancel();
        orderRepository.save(order);
        return BaseResponseUtils.success(null, "Cancel order successfully");
    }

    @GetMapping("/ipn-handler")
    public BaseResponse<Void> inpHandler(@RequestParam Map<String, String> params) {
        int resultCode = Integer.parseInt(params.get("resultCode"));
        if (resultCode == 0) {
            String orderId = params.get("orderId");
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));
            if (!Objects.equals(order.getCurrentStatus().getStatus(), BaseConstant.ORDER_STATUS_PENDING)) {
                return BaseResponseUtils.success(null, "Đơn hàng đã xử lý rồi");
            }
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setStatus(BaseConstant.ORDER_STATUS_PROCESSING);
            orderStatus.setDate(new Date());
            orderStatus.setOrder(order);

            order.setCurrentStatus(orderStatus);
            order.getOrderStatuses().add(orderStatus);
            orderRepository.save(order);
            return BaseResponseUtils.success(null, "Giao dịch thành công");
        }
        return BaseResponseUtils.success(null, "Giao dịch thất bại");
    }
}
