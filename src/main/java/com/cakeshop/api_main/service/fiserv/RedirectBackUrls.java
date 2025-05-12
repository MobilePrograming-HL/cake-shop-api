package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedirectBackUrls {
    private String successUrl;
    private String failureUrl;
}