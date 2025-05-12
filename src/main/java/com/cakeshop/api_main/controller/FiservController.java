package com.cakeshop.api_main.controller;

import com.cakeshop.api_main.dto.response.BaseResponse;
import com.cakeshop.api_main.service.fiserv.*;
import com.cakeshop.api_main.utils.BaseResponseUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/fiserv")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiservController {
    FiservService fiservService;

    @PostMapping("/create")
    public BaseResponse<FiservCreateCheckoutResponse> createCheckout() {
//        FiservCreateCheckoutResponse response = fiservService.create();
//        return BaseResponseUtils.success(response, "Create checkouts successful");
        return null;
    }

    @GetMapping("/success")
    public BaseResponse<Void> success() {
        String message = "Success";
        return BaseResponseUtils.success(null, message);
    }

    @GetMapping("/fail")
    public BaseResponse<Void> fail() {
        String message = "Fail";
        return BaseResponseUtils.success(null, message);
    }

    @PostMapping("/ipn-handler")
    public BaseResponse<Void> ipnHandler(@RequestBody FiservWebhookPayload payload) {
//        log.info("ipn handler called");
//        if ("PRE-AUTH".equals(payload.getTransactionType())
//                && "APPROVED".equals(payload.getTransactionStatus())) {
//
//            String orderId = payload.getOrderId();
//            Long amount = payload.getApprovedAmount().getTotal();
//            String currency = payload.getApprovedAmount().getCurrency();
//            // update status
//            log.info("Capturing transaction [{}] with amount {} {}", orderId, amount, currency);
//            CaptureResponse captureResponse = fiservService.captureByOrderId(orderId, amount, currency);
//            log.info(captureResponse.toString());
//        }
//        return BaseResponseUtils.success(null, "call back");
        return null;
    }

//    @GetMapping("/check-order/{orderId}")
//    public BaseResponse<Void> checkOrder(@PathVariable String orderId) {
//        log.info("ipn handler called");
//        if ("PRE-AUTH".equals(payload.getTransactionType())
//                && "APPROVED".equals(payload.getTransactionStatus())) {
//
//            String orderId = payload.getOrderId();
//            BigDecimal amount = payload.getApprovedAmount().getTotal();
//            String currency = payload.getApprovedAmount().getCurrency();
//            // update status
//            log.info("Capturing transaction [{}] with amount {} {}", orderId, amount, currency);
//            fiservService.captureByOrderId(orderId, amount, currency);
//        }
//        return BaseResponseUtils.success(null, "call back");
//    }
}
