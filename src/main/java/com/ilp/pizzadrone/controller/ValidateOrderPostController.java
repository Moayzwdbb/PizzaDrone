package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.model.OrderValidation;
import com.ilp.pizzadrone.service.OrderService;
import com.ilp.pizzadrone.validation.OrderValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling POST requests related to order validation.
 * This controller is responsible for handling the requests to validate an order.
 */
@RestController
public class ValidateOrderPostController {
    private final OrderService orderService;
    private final OrderValidator orderValidator;

    /**
     * Constructor for the order validation controller class
     */
    public ValidateOrderPostController(OrderService orderService,
                                       OrderValidator orderValidator) {
        this.orderService = orderService;
        this.orderValidator = orderValidator;
    }

    /**
     * Validates the order and returns the validation result.
     *
     * @param order json object containing the order
     * @return the validation result
     */
    @PostMapping("/validateOrder")
    public ResponseEntity<?> validateOrder(@RequestBody Order order) {
        // Validate the order
        ResponseEntity<?> validationResponse = orderValidator.validateOrderRequest(order);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        // If order is valid, validate the order
        OrderValidation orderValidationResult = orderService.validateOrder(order);
        return ResponseEntity.ok(orderValidationResult);
    }
}
