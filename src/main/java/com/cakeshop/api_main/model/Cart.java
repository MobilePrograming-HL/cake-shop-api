package com.cakeshop.api_main.model;

import com.cakeshop.api_main.exception.BadRequestException;
import com.cakeshop.api_main.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(name = "tbl_cart")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart extends Abstract {
    @OneToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToMany(mappedBy = "cart")
    List<CartItem> cartItems = new ArrayList<>();

    public void addItems(Map<Product, Integer> productQuantityMap) {
        for (Map.Entry<Product, Integer> entry : productQuantityMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            addItem(product, quantity);

        }
    }

    public void addItem(Product product, int quantity) {
        if (!product.checkQuantity(quantity)) {
            throw new BadRequestException(
                    "Insufficient quantity for product: " + product.getId(),
                    ErrorCode.INVALID_FORM_ERROR);
        }
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            CartItem newItem = new CartItem(product, quantity, this);
            cartItems.add(newItem);
        }
    }
}
