package com.cakeshop.api_main.model.index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductIndex {
    private String id;
    private String name;
    private Double price;
    private String categoryId;
    private String categoryName;
    private Integer discountPercentage;
    private Long totalSold;
    private Date createdAt;
}

