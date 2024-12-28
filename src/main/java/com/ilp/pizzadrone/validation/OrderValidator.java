package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.dto.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * This class is for validate request information in an order exists.
 * It checks orderData, priceTotalInPence, pizzasInOrder, creditCardInformation.
 */
@Component
public class OrderValidator {
    public ResponseEntity<?> validateOrderRequest(Order order) {
        // Check if the order is not null
        if (order == null || order.getOrderDate() == null ||
                order.getPriceTotalInPence() <= 0 ||
                order.getPizzasInOrder() == null ||
                order.getCreditCardInformation() == null) {
            return ResponseEntity.badRequest().body("Invalid Order: " +
                    "Order data is invalid");
        }

        // return null if validations are pass
        return null;
    }
}
