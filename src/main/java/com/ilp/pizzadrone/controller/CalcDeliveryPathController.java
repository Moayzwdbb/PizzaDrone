package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.service.CalcDeliveryPathService;
import com.ilp.pizzadrone.service.OrderService;
import com.ilp.pizzadrone.validation.OrderValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for the CalcDeliveryPathService
 * This controller is responsible for handling the requests to calculate the delivery path
 */
@RestController
public class CalcDeliveryPathController {
    private final CalcDeliveryPathService calcDeliveryPathService;
    private final OrderService orderService;
    private final OrderValidator orderValidator;

    /**
     * Constructor for the CalcDeliveryPathController
     */
    public CalcDeliveryPathController(CalcDeliveryPathService calcDeliveryPathService,
                                      OrderService orderService,
                                      OrderValidator orderValidator) {
        this.calcDeliveryPathService = calcDeliveryPathService;
        this.orderService = orderService;
        this.orderValidator = orderValidator;
    }

    /**
     * Calculate the delivery path list for the order
     *
     * @param order the order
     * @return the delivery path
     */
    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<?> calcDeliveryPath(@RequestBody Order order) {
        // Validate the order
        ResponseEntity<?> validationResponse = orderValidator.validateOrderRequest(order);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        // Get the validation result
        OrderStatus validationResult = orderService.validateOrder(order).orderStatus();

        // Return bad request if validation fails
        if (validationResult == OrderStatus.INVALID) {
            return ResponseEntity.badRequest().body("Invalid order");
        }

        // Calculate the delivery path
        List<LngLat> flyPath = calcDeliveryPathService.calcDeliveryPath(order);
        return ResponseEntity.ok(flyPath);
    }
}
