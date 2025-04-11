package com.cakeshop.api_main.dto.response.review;

import com.cakeshop.api_main.dto.response.customer.CustomerResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ReviewResponse {
    Long id;
    String productId;
    CustomerResponse customer;
    int rate;
    String content;
    Long total;
    Date createdAt;
}
