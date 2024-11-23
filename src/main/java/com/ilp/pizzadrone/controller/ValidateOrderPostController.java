package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.model.OrderValidation;
import com.ilp.pizzadrone.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateOrderPostController {
    private final OrderService orderService;

    public ValidateOrderPostController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidation> validateOrder(@RequestBody Order order) {
        OrderValidation orderValidationResult = orderService.validateOrder(order);
        return ResponseEntity.ok(orderValidationResult);
    }
}
