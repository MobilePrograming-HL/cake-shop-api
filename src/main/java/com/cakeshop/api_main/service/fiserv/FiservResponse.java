package com.cakeshop.api_main.service.fiserv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FiservResponse {
    String storeId;
    String checkoutId;
    String redirectionUrl;
}
