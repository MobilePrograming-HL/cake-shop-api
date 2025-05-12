package com.cakeshop.api_main.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class BaseService {
    OrderCodeService orderCodeService;

    public String getOrderCode(Long keyNumber){
        return orderCodeService.orderCode(keyNumber);
    }
}
