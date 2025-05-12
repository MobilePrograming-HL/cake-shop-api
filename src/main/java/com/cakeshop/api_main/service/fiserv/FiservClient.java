package com.cakeshop.api_main.service.fiserv;

import jakarta.websocket.server.PathParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "fiservClient", url = "https://prod.emea.api.fiservapps.com/sandbox")
public interface FiservClient {
    @PostMapping(value = "/exp/v1/checkouts", consumes = MediaType.APPLICATION_JSON_VALUE)
    FiservCreateCheckoutResponse createCheckout(
            @RequestHeader("Api-Key") String apiKey,
            @RequestHeader("Client-Request-Id") String clientRequestId,
            @RequestHeader("Timestamp") String timestamp,
            @RequestHeader("Message-Signature") String messageSignature,
            @RequestBody String requestBody
    );

    @GetMapping(value = "/exp/v1/checkouts/{checkoutId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    CheckoutResponse getCheckout(
            @RequestHeader("Api-Key") String apiKey,
            @PathVariable String checkoutId
    );

    @PostMapping(value = "/ipp/payments-gateway/v2/orders/{order-id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    CaptureResponse captureByOrderId(
            @RequestHeader("Api-Key") String apiKey,
            @RequestHeader("Client-Request-Id") String clientRequestId,
            @RequestHeader("Timestamp") String timestamp,
            @RequestHeader("Message-Signature") String messageSignature,
            @PathVariable("order-id") String orderId,
            @RequestBody PostAuthRequest request
    );
}
