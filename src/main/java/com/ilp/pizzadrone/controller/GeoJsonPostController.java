package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.service.CalcDeliveryPathService;
import com.ilp.pizzadrone.service.GeoJsonService;
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

    /**
     * Constructor for the GeoJsonPostController
     */
    public GeoJsonPostController(GeoJsonService geoJsonService,
                                 CalcDeliveryPathService calcDeliveryPathService) {
        this.geoJsonService = geoJsonService;
        this.calcDeliveryPathService = calcDeliveryPathService;
    }

    /**
     * Calculate the delivery path for the order and return it as GeoJSON
     *
     * @param order the order
     * @return the delivery path as GeoJSON
     */
    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<?> calcDeliveryPathAsGeoJson(@RequestBody Order order) {
        // Calculate the delivery path
        List<LngLat> flyPath = calcDeliveryPathService.calcDeliveryPath(order);

        // Convert the path to GeoJSON format
        String geoJson = geoJsonService.convertToGeoJson(flyPath);

        // Return GeoJSON response
        return ResponseEntity.ok(geoJson);
    }
}
