package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptureResponse {
    private String clientRequestId;
    private String apiTraceId;
    private String ipgTransactionId;
    private String orderId;
    private String merchantTransactionId;
    private String transactionStatus;
    private String transactionType;
}