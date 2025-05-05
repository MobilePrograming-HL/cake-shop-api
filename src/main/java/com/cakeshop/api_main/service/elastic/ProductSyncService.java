package com.cakeshop.api_main.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.cakeshop.api_main.constant.BaseConstant;
import com.cakeshop.api_main.dto.response.product.ProductSoldResponse;
import com.cakeshop.api_main.mapper.ProductIndexMapper;
import com.cakeshop.api_main.model.Product;
import com.cakeshop.api_main.model.index.ProductIndex;
import com.cakeshop.api_main.repository.internal.IOrderItemRepository;
import com.cakeshop.api_main.repository.internal.IProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSyncService {
    IProductRepository productRepository;
    IOrderItemRepository orderItemRepository;
    ElasticsearchClient client;

    public void syncAllProducts() throws IOException {
        List<Product> products = productRepository.findAll();
        int success = 0, fail = 0;

        for (Product product : products) {
            try {
                ProductSoldResponse response = orderItemRepository.findSoldQuantityByProductId(
                        product.getId(),
                        BaseConstant.ORDER_STATUS_DELIVERED
                );
                long totalSold = response != null ? response.getTotalSold() : 0L;
                ProductIndex index = ProductIndexMapper.fromProduct(product, totalSold);

                client.index(i -> i
                        .index("products")
                        .id(product.getId())
                        .document(index)
                );

                success++;
            } catch (Exception e) {
                System.err.println("❌ Sync thất bại: " + product.getId() + " - " + e.getMessage());
                fail++;
            }
        }

        System.out.println("✅ Sync xong " + success + " sản phẩm. ❌ Thất bại: " + fail);
    }
}
