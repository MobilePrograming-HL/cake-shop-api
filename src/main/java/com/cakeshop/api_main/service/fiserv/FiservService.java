package com.cakeshop.api_main.service.fiserv;

import com.cakeshop.api_main.dto.response.order.FiservInfoResponse;
import com.cakeshop.api_main.service.id.SnowFlakeIdService;
import com.cakeshop.api_main.utils.HMACUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class FiservService {
    private final FiservClient fiservClient;

    private final String apiKey = "MCFggV7VcRfl36AWcDt36cDNnchYzyxg";
    private final String apiSecret = "ofrgYGAVG9EOBR6fAxvMumuNFhmdv6IvQAqORPSzPot";
    private final String endpoint = "https://514d-2402-800-6f2c-8012-3879-b7fd-fa8d-1666.ngrok-free.app/api/v1/order/fiserv-webhook?restaurentId=821";

    public static final int TWO_HOUR = 2 * 60 * 60 * 1000;
    private List<Integer> numberRand = new ArrayList<>();
    private Map<String, Long> storeOrderSttForCheck = new ConcurrentHashMap<>();

    public FiservCreateCheckoutResponse create(String orderId, String currency, Double amount) {
        CreateCheckoutRequest request = new CreateCheckoutRequest();
        request.setStoreId("72305408");
        request.setTransactionOrigin("ECOM");
        request.setTransactionType("PRE-AUTH");
        Money money = new Money();
        money.setTotal(1.01);
        money.setCurrency("USD");
        request.setTransactionAmount(money);
        OrderFiserv orderFiserv = new OrderFiserv();
        orderFiserv.setOrderId(orderStt(821L));
        request.setOrder(orderFiserv);
        CheckoutSettings settings = new CheckoutSettings();
        settings.setLocale("en_GB");
        RedirectBackUrls redirectBackUrls = new RedirectBackUrls();
        redirectBackUrls.setSuccessUrl("https://www.youtube.com");
        redirectBackUrls.setFailureUrl("https://www.youtube.com/fail");
        settings.setRedirectBackUrls(redirectBackUrls);
        settings.setWebHooksUrl(endpoint);
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

    public CaptureResponse captureByOrderId(String orderId, Double amount, String currency, String merchantTransactionId) {
        PostAuthRequest request = new PostAuthRequest();
        request.setRequestType("PostAuthTransaction");
        request.setMerchantTransactionId(merchantTransactionId);
        OrderFiserv orderFiserv = new OrderFiserv();
        orderFiserv.setOrderId(orderId);
        request.setOrder(orderFiserv);
        Money money = new Money();
        money.setTotal(1.01);
        money.setCurrency(currency);
        request.setTransactionAmount(money);

        String bodyJson = toJson(request); // Serialize body to String
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

//    public static String generateOrderId() {
//        Random random = new Random();
//        long number = 100000000000L + (long)(random.nextDouble() * 899999999999L);
//        return String.valueOf(number);
//    }

    public synchronized String orderStt(Long idRestaurant){
        //delelete key has valule > 2hour
        SecureRandom secureRandom = new SecureRandom();

        if (numberRand.isEmpty()) {
            for (int i = 0; i <= 9; i++) {
                numberRand.add(i);
            }
        }

        Set<String> keys = storeOrderSttForCheck.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            Long value = storeOrderSttForCheck.get(key);
            if((System.currentTimeMillis() - value) >= TWO_HOUR){
                storeOrderSttForCheck.remove(key);
            }
        }


        StringBuilder builder = new StringBuilder();
        secureRandom.setSeed(idRestaurant);

        while(true){
            Collections.shuffle(numberRand);
            for(int i =0; i< 4; i++){
                builder.append(numberRand.get(secureRandom.nextInt(9)));
            }

            String stt = builder.toString();
            if(!storeOrderSttForCheck.containsKey(stt)){
                storeOrderSttForCheck.put(stt,System.currentTimeMillis());
                break;
            }

        }
        return builder.toString();
    }
}
