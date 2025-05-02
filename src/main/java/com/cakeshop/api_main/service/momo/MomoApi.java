package com.cakeshop.api_main.service.momo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "momo", url = "${payment.momo.end-point}")
public interface MomoApi {
    @PostMapping("/create")
    CreateMomoResponse create(@RequestBody CreateMomoRequest request);
}
