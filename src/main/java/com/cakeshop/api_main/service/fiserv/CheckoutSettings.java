package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutSettings {
    private String locale;
    private RedirectBackUrls redirectBackUrls;
    private String webHooksUrl;
}