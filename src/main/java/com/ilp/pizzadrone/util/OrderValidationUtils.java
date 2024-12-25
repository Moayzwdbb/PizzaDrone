package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.dto.Restaurant;
import com.ilp.pizzadrone.service.RetrieveAPIService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for validating orders
 * Includes methods for finding the restaurant from the order pizzas
 */
@Component
public class OrderValidationUtils {
    // Get restaurant list
    private final RetrieveAPIService retrieveAPIService;
    private List<Restaurant> cachedRestaurants;

    /**
     * Constructor for the order validation util class
     */
    public OrderValidationUtils(RetrieveAPIService retrieveAPIService) {
        this.retrieveAPIService = retrieveAPIService;
    }

    /**
     * Finds the restaurant from the order pizzas in restaurant list.
     *
     * @param menuName the name of the pizza
     * @return the restaurant from the order pizzas
     */
    public Restaurant findOrderRestaurant(String menuName) {
        // Get restaurant list with caching
        List<Restaurant> restaurants = getRestaurants();

        // Validate the restaurant list
        if (restaurants == null || restaurants.isEmpty()) {
            throw new IllegalStateException("Restaurant list is unavailable");
        }

        // Find the matching restaurant
        return restaurants.stream()
                .filter(restaurant -> Arrays.stream(restaurant.menu())
                        .anyMatch(menuItem -> menuItem.name().equals(menuName)))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the cached restaurant list or fetches it if not available.
     *
     * @return the list of restaurants
     */
    private List<Restaurant> getRestaurants() {
        if (cachedRestaurants == null) {
            cachedRestaurants = retrieveAPIService.fetchRestaurants();
        }
        return cachedRestaurants;
    }
}
