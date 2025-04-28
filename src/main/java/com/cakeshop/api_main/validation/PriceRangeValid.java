package com.cakeshop.api_main.validation;

import com.cakeshop.api_main.validation.impl.PriceRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceRangeValid {
    String message() default "From price must be less than or equal to To price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
