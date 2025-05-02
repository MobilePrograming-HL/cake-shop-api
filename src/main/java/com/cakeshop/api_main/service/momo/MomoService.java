package com.cakeshop.api_main.service.momo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MomoService {
    @Value("${payment.momo.access-key}")
    String accessKey;

    @Value("${payment.momo.secret-key}")
    String secretKey;

    @Value("${payment.momo.partner-code}")
    String partnerCode;

    @Value("${payment.momo.redirect-url}")
    String redirectUrl;

    @Value("${payment.momo.ipn-url}")
    String ipnUrl;

    @Value("${payment.momo.request-type}")
    String requestType;

    String extraData = "";
    String lang = "vi";
    Integer orderExpireTime = 5;

    @Autowired
    MomoApi momoApi;

    public CreateMomoResponse createPaymentUrl(Long amount, String orderId) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderInfo = URLEncoder.encode("Thanh toán đơn hàng " + orderId, StandardCharsets.UTF_8);

        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=%s" +
                        "&ipnUrl=%s&orderId=%s&orderInfo=%s" +
                        "&partnerCode=%s&redirectUrl=%s" +
                        "&requestId=%s&requestType=%s",
                accessKey, amount, extraData, ipnUrl, orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType
        );

        String signature = "";
        try {
            signature = signHmacSHA256(rawSignature, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreateMomoRequest request = new CreateMomoRequest();
        request.setPartnerCode(partnerCode);
        request.setRequestType(requestType);
        request.setIpnUrl(ipnUrl);
        request.setOrderId(orderId);
        request.setAmount(amount);
        request.setOrderInfo(orderInfo);
        request.setRequestId(requestId);
        request.setRedirectUrl(redirectUrl);
        request.setLang(lang);
        request.setExtraData(extraData);
        request.setSignature(signature);

        return momoApi.create(request);
    }

    private String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
