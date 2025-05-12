package com.cakeshop.api_main.dto.response.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiservInfoResponse {
    private String storeId;
    private String checkoutId;
    private String redirectionUrl;
}
