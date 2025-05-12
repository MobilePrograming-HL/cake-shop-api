package com.cakeshop.api_main.service.fiserv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAuthRequest {
    private String requestType;
    private String merchantTransactionId;
    private OrderFiserv order;
    private Money transactionAmount;
}