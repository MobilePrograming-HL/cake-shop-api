package com.cakeshop.api_main.mapper;

import com.cakeshop.api_main.dto.response.category.CategoryResponse;
import com.cakeshop.api_main.dto.response.discount.DiscountResponse;
import com.cakeshop.api_main.dto.response.product.ProductResponse;
import com.cakeshop.api_main.model.Product;
import com.cakeshop.api_main.model.index.ProductIndex;

import java.util.List;

public class ProductIndexMapper {

    public static ProductIndex fromProduct(Product product, long totalSold) {
        return ProductIndex.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .discountPercentage(product.getDiscountPercentage())
                .totalSold(totalSold)
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static ProductResponse toProductResponse(ProductIndex index) {
        ProductResponse res = new ProductResponse();
        res.setId(index.getId());
        res.setName(index.getName());
        res.setPrice(index.getPrice());
        res.setTotalSold(index.getTotalSold());
        res.setCategory(CategoryResponse.builder()
                .id(index.getCategoryId())
                .name(index.getCategoryName())
                .build());
        res.setDiscount(DiscountResponse.builder()
                .discountPercentage(index.getDiscountPercentage())
                .build());
        return res;
    }

    public static List<ProductResponse> toProductResponseList(List<ProductIndex> list) {
        return list.stream()
                .map(ProductIndexMapper::toProductResponse)
                .toList();
    }
}
