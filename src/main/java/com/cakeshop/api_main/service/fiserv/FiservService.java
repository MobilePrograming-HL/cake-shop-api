package com.cakeshop.api_main.service.fiserv;

import com.cakeshop.api_main.service.id.SnowFlakeIdService;
import com.cakeshop.api_main.utils.HMACUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class FiservService {
    @Autowired
    private FiservClient fiservClient;

    @Value(value = "${payment.fiserv.apiKey}")
    private String apiKey;
    @Value(value = "${payment.fiserv.apiSecret}")
    private String apiSecret;
    @Value(value = "${payment.fiserv.callback-url}")
    private String callbackUrl;
    @Value(value = "${payment.fiserv.storeId}")
    private String storeId;
    private String webhookEndpoint = "/api/v1/order/fiserv-webhook?restaurantId=821";
    private String successEndpoint = "/api/v1/fiserv/payment-success";
    private String failureEndpoint = "/api/v1/fiserv/payment-fail";

    private static final double VND_TO_USD_RATE = 24000.0;

    public FiservCreateCheckoutResponse create(String orderId, String currency, Double amount) {
        CreateCheckoutRequest request = new CreateCheckoutRequest();
        request.setStoreId(storeId);
        request.setTransactionOrigin("ECOM");
        request.setTransactionType("PRE-AUTH");
        Money money = new Money();
        money.setTotal(convertVndToUsd(amount));
        money.setCurrency(currency);
        request.setTransactionAmount(money);
        OrderFiserv orderFiserv = new OrderFiserv();
        orderFiserv.setOrderId(orderId);
        request.setOrder(orderFiserv);
        CheckoutSettings settings = new CheckoutSettings();
        settings.setLocale("en_GB");
        RedirectBackUrls redirectBackUrls = new RedirectBackUrls();
        redirectBackUrls.setSuccessUrl(callbackUrl + successEndpoint);
        redirectBackUrls.setFailureUrl(callbackUrl + failureEndpoint);
        settings.setRedirectBackUrls(redirectBackUrls);
        settings.setWebHooksUrl(callbackUrl + webhookEndpoint);
        request.setCheckoutSettings(settings);
        String requestBody = toJson(request);

        log.info(requestBody);

        String clientRequestId = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String messageToSign = apiKey + clientRequestId + timestamp + requestBody;
        String signature = HMACUtils.encrypt(messageToSign, apiSecret, "HmacSHA256");

        return fiservClient.createCheckout(apiKey, clientRequestId, timestamp, signature, requestBody);
    }

    public CheckoutResponse getCheckout(String checkoutId) {
        return fiservClient.getCheckout(apiKey, checkoutId);
    }

    public CaptureResponse captureByOrderId(String orderId, Double amount, String currency) {
        PostAuthRequest request = new PostAuthRequest();
        request.setRequestType("PostAuthTransaction");
        OrderFiserv orderFiserv = new OrderFiserv();
        orderFiserv.setOrderId(orderId);
        request.setOrder(orderFiserv);
        Money money = new Money();
        money.setTotal(amount);
        money.setCurrency(currency);
        request.setTransactionAmount(money);

        String bodyJson = toJson(request);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String requestId = UUID.randomUUID().toString();
        String toSign = apiKey + requestId + timestamp + bodyJson;
        String signature = HMACUtils.encrypt(toSign, apiSecret, "HmacSHA256");

        return fiservClient.captureByOrderId(apiKey, requestId, timestamp, signature, orderId, request);
    }

    private String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize Fiserv request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to serialize request", e);
        }
    }

//    public static String generateOrderId() {
//        Random random = new Random();
//        long part1 = 1000000L + (long)(random.nextDouble() * 8999999L);   // 7 chữ số
//        long part2 = 10000000L + (long)(random.nextDouble() * 89999999L); // 8 chữ số
//        return String.valueOf(part1) + String.valueOf(part2); // Tổng cộng 15 chữ số
//    }

    public String generateShortOrderId() {
        SnowFlakeIdService.getInstance().setDataCenterId(0);
        SnowFlakeIdService.getInstance().setNodeId(0);
        long id = SnowFlakeIdService.getInstance().nextId();
        return Long.toHexString(id).substring(0, 13); // cắt bớt nếu cần rút gọn
    }

    private double convertVndToUsd(double vndAmount) {
        return Math.round((vndAmount / VND_TO_USD_RATE) * 100.0) / 100.0;
    }
}
