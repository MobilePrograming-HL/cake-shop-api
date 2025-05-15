package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCheckoutRequest {
    private String storeId;
    private String transactionOrigin;
    private String transactionType;
    private Money transactionAmount;
    private CheckoutSettings checkoutSettings;
    private OrderFiserv order;
}
