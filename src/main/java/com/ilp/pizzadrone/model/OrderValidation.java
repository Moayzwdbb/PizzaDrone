package com.ilp.pizzadrone.model;

import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.constant.OrderValidationCode;

/**
 * This class is a OrderValidation object that is used to validate an order.
 * It contains the order status and the order validation code.
 */
public record OrderValidation(OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
}
