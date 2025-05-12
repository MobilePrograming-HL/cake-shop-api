package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutResponse {
    private String storeId;
    private String checkoutId;
    private String transactionType;
    private RequestSent requestSent;

    @Getter
    @Setter
    public class RequestSent {
        private String merchantTransactionId;
    }
}
