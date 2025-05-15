package com.cakeshop.api_main.service.momo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMomoResponse {
    String partnerCode;
    String requestId;
    String orderId;
    Long amount;
    Long responseTime;
    String message;
    Integer resultCode;
    String payUrl;
    String shortLink;
}
