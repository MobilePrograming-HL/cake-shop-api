package com.cakeshop.api_main.service.momo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMomoRequest {
    String partnerCode;
    String requestType;
    String ipnUrl;
    String orderId;
    Long amount;
    String orderInfo;
    String requestId;
    String redirectUrl;
    String lang;
    String extraData;
    String signature;
}
