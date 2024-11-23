package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.constant.OrderValidationCode;
import com.ilp.pizzadrone.dto.Pizza;
import com.ilp.pizzadrone.dto.Restaurant;
import com.ilp.pizzadrone.service.RestaurantService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for validating orders
 * Includes methods for validating the order, pizzas, restaurant, credit card, and total price
 */
@Component
public class OrderValidationUtil {
    // Get restaurant list
    private final RestaurantService restaurantService;

    public OrderValidationUtil(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * VChecks if all pizzas in the order are from the same restaurant.
     *
     * @param pizzas the pizzas in the order
     * @return True if all pizzas are from the same restaurant, false otherwise
     */
    public boolean allPizzaFromSingleRestaurant(Pizza[] pizzas) {
        // Get first order's restaurant number
        char restaurantNum = pizzas[0].name().charAt(1);

        // Check if all orders are from the same restaurant
        return Arrays.stream(pizzas).allMatch(pizza -> pizza.name().charAt(1) == restaurantNum);
    }

    /**
     * Checks if the total price of the order is correct.
     *
     * @param pizzas            the pizzas in the order
     * @param priceTotalInPence the total price of the order in pence
     * @return True if the total price is correct, false otherwise
     */
    public boolean isTotalPriceCorrect(Pizza[] pizzas, int priceTotalInPence) {
        // Calculate the total price of the pizzas
        int total = Arrays.stream(pizzas).mapToInt(Pizza::priceInPence).sum();

        // Compare pence are same added extra 100 pence delivery charge
        return priceTotalInPence == total + 100;
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
        Restaurant orderRestaurant = findOrderRestaurant(pizzas);

        // If the restaurant is not found, return false
        if (orderRestaurant == null) return false;

        // Check if the restaurant is open on the given order day
        return Arrays.asList(orderRestaurant.openingDays()).contains(orderDay);
    }


    /**
     * Checks if the credit card expiry date is valid.
     *
     * @param expiryDate the credit card expiry date
     * @return True if the credit card expiry date is valid, false otherwise
     */
    public boolean isValidExpiryDate(String expiryDate) {
        // Expiry date should be in MM/YY format
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        // Get expiry month and year
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000;

        // Check month is valid
        if (month < 1 || month > 12) return false;

        // Check if the expiry date is in the future
        LocalDate expiry = LocalDate.of(year, month, 1).withDayOfMonth(1).plusMonths(1);
        return expiry.isAfter(LocalDate.now());
    }


    /**
     * Validates the pizza in the order. Checks if the pizza price and name matches the menu of the restaurant.
     * @param pizzas the pizzas in the order
     * @return the validation result. If the pizza is valid, return NO_ERROR.
     * If the pizza price is invalid, return PRICE_FOR_PIZZA_INVALID.
     * If the pizza is not defined, return PIZZA_NOT_DEFINED.
     */
    public OrderValidationCode isValidPizza(Pizza[] pizzas) {
        // Find the matching restaurant
        Restaurant OrderRestaurant = findOrderRestaurant(pizzas);

        // If restaurant not found, return false
        if (OrderRestaurant == null) {
            return OrderValidationCode.PIZZA_NOT_DEFINED;
        }

        // Check if the pizza price and pizza name matches the menu of restaurant
        for (Pizza pizza : pizzas) {
            boolean isPizzaPriceValid = Arrays.stream(OrderRestaurant.menu())
                    .anyMatch(menuItem -> menuItem.priceInPence() == pizza.priceInPence());

            boolean isPizzaValid = Arrays.stream(OrderRestaurant.menu())
                    .anyMatch(menuItem -> menuItem.name().equals(pizza.name()));


            // If any pizza is invalid, return PRICE_FOR_PIZZA_INVALID
            if (!isPizzaPriceValid) {
                return OrderValidationCode.PRICE_FOR_PIZZA_INVALID;
            }

            // If any pizza is invalid, return PIZZA_NOT_DEFINED
            if (!isPizzaValid) {
                return OrderValidationCode.PIZZA_NOT_DEFINED;
            }
        }

        // If all pizzas are valid, return NO_ERROR
        return OrderValidationCode.NO_ERROR;
    }

    /**
     * Finds the restaurant from the order pizzas in restaurant list.
     *
     * @param pizzas the pizzas in the order
     * @return the restaurant from the order pizzas
     */
    public Restaurant findOrderRestaurant(Pizza[] pizzas) {
        // Get restaurant number from pizza name
        char restaurantNum = pizzas[0].name().charAt(1);

        // Get restaurant list
        List<Restaurant> restaurants = restaurantService.fetchRestaurants();

        // Find the matching restaurant
        return restaurants.stream()
                .filter(restaurant -> restaurant.menu()[0].name().contains("R" + restaurantNum))
                .findFirst()
                .orElse(null);
    }

}
