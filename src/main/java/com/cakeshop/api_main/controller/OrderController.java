package com.cakeshop.api_main.controller;

import com.cakeshop.api_main.constant.BaseConstant;
import com.cakeshop.api_main.dto.request.order.BuyNowOrderRequest;
import com.cakeshop.api_main.dto.request.order.CreateOrderRequest;
import com.cakeshop.api_main.dto.request.order.UpdateOrderStatusRequest;
import com.cakeshop.api_main.dto.request.orderItem.OrderItemDetails;
import com.cakeshop.api_main.dto.response.BaseResponse;
import com.cakeshop.api_main.dto.response.PaginationResponse;
import com.cakeshop.api_main.dto.response.order.MomoInfoResponse;
import com.cakeshop.api_main.dto.response.order.OrderResponse;
import com.cakeshop.api_main.dto.response.orderStatus.OrderStatusResponse;
import com.cakeshop.api_main.exception.BadRequestException;
import com.cakeshop.api_main.exception.ErrorCode;
import com.cakeshop.api_main.exception.NotFoundException;
import com.cakeshop.api_main.mapper.OrderMapper;
import com.cakeshop.api_main.mapper.OrderStatusMapper;
import com.cakeshop.api_main.model.*;
import com.cakeshop.api_main.model.criteria.OrderCriteria;
import com.cakeshop.api_main.repository.internal.*;
import com.cakeshop.api_main.service.BaseService;
import com.cakeshop.api_main.service.fiserv.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/order")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderController {
    @Autowired
    ICustomerRepository customerRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    ITagRepository tagRepository;
    @Autowired
    IAddressRepository addressRepository;
    @Autowired
    ICartItemRepository cartItemRepository;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Autowired
    MomoService momoService;
    @Autowired
    FiservService fiservService;
    @Autowired
    BaseService baseService;

    @Value(value = "${order.keyNumer}")
    Long keyNumber;

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
    public BaseResponse<OrderResponse> buyNow(@Valid @RequestBody BuyNowOrderRequest request) {
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
        String code = baseService.getOrderCode(keyNumber);
        Order order = new Order(customer, request.getShippingFee(), request.getPaymentMethod(), address, request.getNote(), code);
        int status;
        if (Objects.equals(request.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO) ||
                Objects.equals(request.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_FISERV)) {
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
        OrderResponse orderResponse = orderMapper.fromEntityToOrderResponse(order);

        if (Objects.equals(order.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO)) {
            try {
                Long amount = Math.round(order.getTotalAmount());
                CreateMomoResponse response = momoService.createPaymentUrl(amount, order.getId());
                MomoInfoResponse payUrlResponse = new MomoInfoResponse();
                payUrlResponse.setPayUrl(response.getPayUrl());
                orderResponse.setMomoInfo(payUrlResponse);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException(ErrorCode.CREATE_PAYMENT_ERROR);
            }
        } else if (Objects.equals(order.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_FISERV)) {
            Double amount = order.getTotalAmount();
            FiservCreateCheckoutResponse response = fiservService.create(order.getId(), "EUR", amount);
            orderResponse.setFiservInfo(response);
        }
        return BaseResponseUtils.success(orderResponse, "Create order successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        String username = SecurityUtil.getCurrentUsername();
        Customer customer = customerRepository.findByAccountUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND_ERROR));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND_ERROR));

        List<CartItem> cartItems = cartItemRepository.findAllByIdsAndUsername(request.getCartItemIds(), username);
        OrderResponse orderResponse = null;
        if (!cartItems.isEmpty()) {
            List<OrderItemDetails> orderItemDetailsList = cartItems.stream()
                    .map(
                            item -> new OrderItemDetails(item.getProduct(), item.getTag(), item.getQuantity())
                    ).toList();
            String code = baseService.getOrderCode(keyNumber);
            Order order = new Order(customer, request.getShippingFee(), request.getPaymentMethod(), address, request.getNote(), code);
            int status;
            if (Objects.equals(request.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO)
                    || Objects.equals(request.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_FISERV)) {
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
            orderResponse = orderMapper.fromEntityToOrderResponse(order);
            if (Objects.equals(order.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_MOMO)) {
                try {
                    Long amount = Math.round(order.getTotalAmount());
                    CreateMomoResponse response = momoService.createPaymentUrl(amount, order.getId());
                    MomoInfoResponse payUrlResponse = new MomoInfoResponse();
                    payUrlResponse.setPayUrl(response.getPayUrl());
                    orderResponse.setMomoInfo(payUrlResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BadRequestException(ErrorCode.CREATE_PAYMENT_ERROR);
                }
            } else if (Objects.equals(order.getPaymentMethod(), BaseConstant.PAYMENT_METHOD_FISERV)) {
                Double amount = order.getTotalAmount();
                FiservCreateCheckoutResponse response = fiservService.create(order.getId(), "", amount);
                orderResponse.setFiservInfo(response);
            }
            cartItemRepository.deleteAll(cartItems);
        }
        return BaseResponseUtils.success(orderResponse, "Create order successfully");
    }

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

    @PostMapping("/fiserv-webhook")
    public BaseResponse<Void> ipnHandler(@RequestBody FiservWebhookPayload payload, @RequestParam Long restaurentId) {
        log.info("ipn handler called");
        if ("PRE-AUTH".equals(payload.getTransactionType())
                && "APPROVED".equals(payload.getTransactionStatus())) {
            log.info("restaurentId = ", restaurentId);
            String orderId = payload.getOrderId();
            Double amount = payload.getApprovedAmount().getTotal();
            String currency = payload.getApprovedAmount().getCurrency();
            String checkoutId = payload.getCheckoutId();

            CheckoutResponse checkoutResponse = fiservService.getCheckout(checkoutId);

            // update status
            String merchantTransactionId = checkoutResponse.getRequestSent().getMerchantTransactionId();
            CaptureResponse captureResponse = fiservService.captureByOrderId(orderId, amount, currency, merchantTransactionId);
            if (captureResponse != null && captureResponse.getTransactionType().equals("POSTAUTH") && captureResponse.getTransactionStatus().equals("APPROVED")) {
                Order order = orderRepository.findById(captureResponse.getMerchantTransactionId())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND_ERROR));
                log.info(order.toString());
            }
        }
        return BaseResponseUtils.success(null, "call back");
    }
}
