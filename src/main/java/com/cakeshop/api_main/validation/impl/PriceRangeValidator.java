package com.cakeshop.api_main.validation.impl;

import com.cakeshop.api_main.model.criteria.ProductCriteria;
import com.cakeshop.api_main.validation.PriceRangeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceRangeValidator implements ConstraintValidator<PriceRangeValid, ProductCriteria> {
    @Override
    public boolean isValid(ProductCriteria criteria, ConstraintValidatorContext context) {
        if (criteria == null) {
            return true;
        }
        Double fromPrice = criteria.getFromPrice();
        Double toPrice = criteria.getToPrice();

        if (fromPrice != null && toPrice != null) {
            return toPrice >= fromPrice;
        }
        return true;
    }
}
