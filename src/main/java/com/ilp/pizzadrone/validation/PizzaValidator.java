package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.constant.OrderValidationCode;
import com.ilp.pizzadrone.dto.Pizza;
import com.ilp.pizzadrone.dto.Restaurant;
import com.ilp.pizzadrone.util.OrderValidationUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.ilp.pizzadrone.constant.SystemConstants.ORDER_CHARGE_IN_PENCE;

/**
 * Utility class for validating pizzas
 * Includes methods for validating the pizza and total price
 */
@Component
public class PizzaValidator {
    private final OrderValidationUtil orderValidationUtil;

    /**
     * Constructor for the pizza validator class
     */
    public PizzaValidator(OrderValidationUtil orderValidationUtil) {
        this.orderValidationUtil = orderValidationUtil;
    }


    /**
     * Validates the pizza in the order. Checks if the pizza price and name matches the menu of the restaurant.
     * @param pizzas the pizzas in the order
     * @return the validation result. If the pizza is valid, return NO_ERROR.
     * If the pizza price is invalid, return PRICE_FOR_PIZZA_INVALID.
     * If the pizza is not defined, return PIZZA_NOT_DEFINED.
     */
    public OrderValidationCode isValidPizza(Pizza[] pizzas) {
        // For each piazza in order, find the matching restaurant
        for (Pizza pizza : pizzas) {
            Restaurant OrderRestaurant = orderValidationUtil.findOrderRestaurant(pizza.name().charAt(1));

            // If restaurant not found, return false
            if (OrderRestaurant == null) {
                return OrderValidationCode.PIZZA_NOT_DEFINED;
            }

            // Check if the pizza defined in the restaurant menu
            boolean isPizzaValid = Arrays.stream(OrderRestaurant.menu())
                    .anyMatch(menuItem -> menuItem.name().equals(pizza.name()));

            // If pizza is not defined, return PIZZA_NOT_DEFINED
            if (!isPizzaValid) {
                return OrderValidationCode.PIZZA_NOT_DEFINED;
            }

            // Check if the pizza price is valid
            boolean isPizzaPriceValid = Arrays.stream(OrderRestaurant.menu())
                    .anyMatch(menuItem -> menuItem.priceInPence() == pizza.priceInPence());

            // If pizza price is invalid, return PRICE_FOR_PIZZA_INVALID
            if (!isPizzaPriceValid) {
                return OrderValidationCode.PRICE_FOR_PIZZA_INVALID;
            }
        }

        // If all pizzas are valid, return NO_ERROR
        return OrderValidationCode.NO_ERROR;
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
        return priceTotalInPence == total + ORDER_CHARGE_IN_PENCE;
    }
}
