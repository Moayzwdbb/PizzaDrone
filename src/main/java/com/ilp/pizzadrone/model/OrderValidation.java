package com.ilp.pizzadrone.model;

import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.constant.OrderValidationCode;

public record OrderValidation(OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
}
