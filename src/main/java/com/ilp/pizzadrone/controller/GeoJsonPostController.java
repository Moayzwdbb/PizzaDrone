package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.service.CalcDeliveryPathService;
import com.ilp.pizzadrone.service.GeoJsonService;
import com.ilp.pizzadrone.service.OrderService;
import com.ilp.pizzadrone.validation.OrderValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for the GeoJsonService
 * This controller is responsible for handling the requests to calculate the delivery path as GeoJSON
 */
@RestController
public class GeoJsonPostController {
    private final GeoJsonService geoJsonService;
    private final CalcDeliveryPathService calcDeliveryPathService;
    private final OrderValidator orderValidator;
    private final OrderService orderService;

    /**
     * Constructor for the GeoJsonPostController
     */
    public GeoJsonPostController(GeoJsonService geoJsonService,
                                 CalcDeliveryPathService calcDeliveryPathService,
                                 OrderValidator orderValidator, OrderService orderService) {
        this.geoJsonService = geoJsonService;
        this.calcDeliveryPathService = calcDeliveryPathService;
        this.orderValidator = orderValidator;
        this.orderService = orderService;
    }

    /**
     * Calculate the delivery path for the order and return it as GeoJSON
     *
     * @param order the order
     * @return the delivery path as GeoJSON
     */
    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<?> calcDeliveryPathAsGeoJson(@RequestBody Order order) {
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

        // Convert the path to GeoJSON format
        String geoJson = geoJsonService.convertToGeoJson(flyPath);

        // Return GeoJSON response
        return ResponseEntity.ok(geoJson);
    }
}
