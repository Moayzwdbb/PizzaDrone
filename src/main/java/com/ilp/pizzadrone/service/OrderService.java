package com.ilp.pizzadrone.service;


import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.constant.OrderValidationCode;
import com.ilp.pizzadrone.dto.CreditCardInformation;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.dto.Pizza;
import com.ilp.pizzadrone.model.OrderValidation;
import com.ilp.pizzadrone.validation.CreditCardValidator;
import com.ilp.pizzadrone.validation.PizzaValidator;
import com.ilp.pizzadrone.validation.RestaurantValidator;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ilp.pizzadrone.constant.SystemConstants.MAX_PIZZAS_PER_ORDER;

/**
 * Service class for validating an order
 */
@Service
public class OrderService {
    private final PizzaValidator pizzaValidator;
    private final CreditCardValidator creditCardValidator;
    private final RestaurantValidator restaurantValidator;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(PizzaValidator pizzaValidator,
                        CreditCardValidator creditCardValidator,
                        RestaurantValidator restaurantValidator) {

        this.pizzaValidator = pizzaValidator;
        this.creditCardValidator = creditCardValidator;
        this.restaurantValidator = restaurantValidator;
    }

    /**
     * Validates an order
     * Checks if the order is valid based on the following criteria:
     * - Order is not empty
     * - Order has at most 4 pizzas
     * - All pizzas are from the same restaurant
     * - Total price is correct
     * - Credit card information is valid
     * - Restaurant is open on the order day
     * - Price of pizza is valid
     *
     * @param order the order to validate
     * @return the validation result
     */
    public OrderValidation validateOrder(Order order) {
        // Get order information
        LocalDate orderDate = order.getOrderDate();
        int priceTotalInPence = order.getPriceTotalInPence();

        // Get Pizza information
        Pizza[] pizzas = order.getPizzasInOrder();

        // Get Credit Card information
        CreditCardInformation creditCardInformation = order.getCreditCardInformation();
        String cardNumber = creditCardInformation.getCreditCardNumber();
        String expiryDate = creditCardInformation.getCreditCardExpiry();
        String cvv = creditCardInformation.getCvv();
        String cardLast4 = (cardNumber != null && cardNumber.length() >= 4)
                ? cardNumber.substring(cardNumber.length() - 4)
                : "N/A";

        // Log the order validation attempt
        log.info("validateOrder called: date={}, pizzasCount={}, totalPriceInPence={}, cardLast4={}",
                orderDate, pizzas != null ? pizzas.length : 0, priceTotalInPence, cardLast4);

        // Check if the order is empty (no pizza)
        if (pizzas == null || pizzas.length == 0) {
            log.warn("validateOrder failed: {}", OrderValidationCode.EMPTY_ORDER);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.EMPTY_ORDER);
        }

        // Check if max pizza count exceeded
        if (pizzas.length > MAX_PIZZAS_PER_ORDER) {
            log.warn("validateOrder failed: {}", OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
        }

        // Check if price of pizza is invalid or not defined
        OrderValidationCode validationCode = pizzaValidator.isValidPizza(pizzas);
        if (validationCode != OrderValidationCode.NO_ERROR) {
            log.warn("validateOrder failed: {}", validationCode);
            return new OrderValidation(OrderStatus.INVALID, validationCode);
        }

        // Check if pizzas are from single restaurant
        if (!restaurantValidator.allPizzaFromSingleRestaurant(pizzas)) {
            log.warn("validateOrder failed: {}", OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }

        // Check if total price is correct
        if (!pizzaValidator.isTotalPriceCorrect(pizzas, priceTotalInPence)) {
            log.warn("validateOrder failed: {}", OrderValidationCode.TOTAL_INCORRECT);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);
        }

        // Check if credit card information is valid (invalid card number)
        if (!cardNumber.matches("\\d{16}")) {
            log.warn("validateOrder failed: {} (cardLength={})",
                    OrderValidationCode.CARD_NUMBER_INVALID,
                    cardNumber != null ? cardNumber.length() : 0);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.CARD_NUMBER_INVALID);
        }

        // Check if credit card information is valid (invalid expiry date)
        if (!creditCardValidator.isValidExpiryDate(expiryDate, orderDate)) {
            log.warn("validateOrder failed: {} (expiryDate={})",
                    OrderValidationCode.EXPIRY_DATE_INVALID,
                    expiryDate);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Check if credit card information is valid (invalid CVV)
        if (!cvv.matches("\\d{3}")) {
            log.warn("validateOrder failed: {} (cvvLength={})",
                    OrderValidationCode.CVV_INVALID,
                    cvv != null ? cvv.length() : 0);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.CVV_INVALID);
        }

        // Check if restaurant is closed on the order day
        DayOfWeek orderDayOfWeek = orderDate.getDayOfWeek();
        if (!restaurantValidator.isRestaurantOpen(pizzas, orderDayOfWeek)) {
            log.warn("validateOrder failed: {} (orderDay={})",
                    OrderValidationCode.RESTAURANT_CLOSED,
                    orderDayOfWeek);
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.RESTAURANT_CLOSED);
        }

        // Return valid order result if passed all checks
        log.info("validateOrder succeeded: {}", OrderValidationCode.NO_ERROR);
        return new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
    }
}
