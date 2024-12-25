package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.dto.Pizza;
import com.ilp.pizzadrone.dto.Restaurant;
import com.ilp.pizzadrone.util.OrderValidationUtils;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for validating restaurants
 * Includes methods for validating the orders from same restaurant and opening days
 */
@Component
public class RestaurantValidator {
    private final OrderValidationUtils orderValidationUtils;

    /**
     * Constructor for the restaurant validator class
     */
    public RestaurantValidator(OrderValidationUtils orderValidationUtils) {
        this.orderValidationUtils = orderValidationUtils;
    }


    /**
     * Checks if all pizzas in the order are from the same restaurant.
     *
     * @param pizzas the pizzas in the order
     * @return True if all pizzas are from the same restaurant, false otherwise
     */
    public boolean allPizzaFromSingleRestaurant(Pizza[] pizzas) {
        // Use a Set to store restaurant name in order
        Set<Restaurant> uniqueRestaurants = new HashSet<>();

        for (Pizza pizza : pizzas) {
            // Find the restaurant from the order pizzas
            Restaurant orderRestaurant = orderValidationUtils.findOrderRestaurant(pizza.name());

            // If the restaurant is not found, return false
            if (orderRestaurant == null) return false;

            uniqueRestaurants.add(orderRestaurant);
        }

        // If there is only one unique restaurant, all pizzas are from the same restaurant
        return uniqueRestaurants.size() == 1;
    }

    /**
     * Checks if the restaurant is open on the given day.
     *
     * @param pizzas    the pizzas in the order
     * @param orderDay  the day of week in the order
     * @return True if the restaurant is open on the given day, false otherwise
     */
    public boolean isRestaurantOpen(Pizza[] pizzas, DayOfWeek orderDay) {
        // Get restaurant list
        Restaurant orderRestaurant = orderValidationUtils.findOrderRestaurant(pizzas[0].name());

        // If the restaurant is not found, return false
        if (orderRestaurant == null) return false;

        // Check if the restaurant is open on the given order day
        return Arrays.asList(orderRestaurant.openingDays()).contains(orderDay);
    }
}
