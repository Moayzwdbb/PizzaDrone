package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.util.CalcPathUtils;
import com.ilp.pizzadrone.util.OrderValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LAT;
import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LNG;

/**
 * Service class to calculate the delivery path for the order
 */
@Service
public class CalcDeliveryPathService {
    private final OrderValidationUtils orderValidationUtils;
    private final RetrieveAPIService retrieveAPIService;
    private final CalcPathUtils calcPathUtils;

    /**
     * Constructor for the CalcDeliveryPathService
     */
    public CalcDeliveryPathService(OrderValidationUtils orderValidationUtils,
                                   RetrieveAPIService retrieveAPIService,
                                   CalcPathUtils calcPathUtils) {

        this.orderValidationUtils = orderValidationUtils;
        this.retrieveAPIService = retrieveAPIService;
        this.calcPathUtils = calcPathUtils;
    }

    /**
     * Calculate the delivery path for the order
     *
     * @param validOrder the valid order
     * @return the delivery path
     */
    public List<LngLat> calcDeliveryPath (Order validOrder) {
        // Location of Appleton Tower
        LngLat appletonTowerLocation = new LngLat(APPLETON_LNG, APPLETON_LAT);

        // Get LnLat of the restaurant from the order
        LngLat orderRestaurantLocation = orderValidationUtils
                .findOrderRestaurant(validOrder.getPizzasInOrder()[0].name())
                .location();

        // Get no-fly zones
        List<NamedRegion> noFlyZones = retrieveAPIService.fetchNoFlyZones();

        // Get central area
        NamedRegion centralArea = retrieveAPIService.fetchCentralArea();

        // Calculate the delivery path
        return calcPathUtils.calculatePath(orderRestaurantLocation, appletonTowerLocation, noFlyZones, centralArea);
    }
}
