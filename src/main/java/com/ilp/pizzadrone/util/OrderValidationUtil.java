package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.dto.Restaurant;
import com.ilp.pizzadrone.service.RestaurantService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for validating orders
 * Includes methods for finding the restaurant from the order pizzas
 */
@Component
public class OrderValidationUtil {
    // Get restaurant list
    private final RestaurantService restaurantService;

    /**
     * Constructor for the order validation util class
     */
    public OrderValidationUtil(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Finds the restaurant from the order pizzas in restaurant list.
     *
     * @param restaurantNum the restaurant number
     * @return the restaurant from the order pizzas
     */
    public Restaurant findOrderRestaurant(char restaurantNum) {
        // Get restaurant list
        List<Restaurant> restaurants = restaurantService.fetchRestaurants();

        // Find the matching restaurant
        return restaurants.stream()
                .filter(restaurant -> restaurant.menu()[0].name().contains("R" + restaurantNum))
                .findFirst()
                .orElse(null);
    }
}
