package com.ilp.pizzadrone.service;


import com.ilp.pizzadrone.constant.OrderStatus;
import com.ilp.pizzadrone.constant.OrderValidationCode;
import com.ilp.pizzadrone.dto.CreditCardInformation;
import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.dto.Pizza;
import com.ilp.pizzadrone.model.OrderValidation;
import com.ilp.pizzadrone.util.OrderValidationUtil;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class OrderService {

    private final OrderValidationUtil orderValidationUtil;

    public OrderService(OrderValidationUtil orderValidationUtil) {
        this.orderValidationUtil = orderValidationUtil;
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

        // Check if the order is empty (no pizza)
        if (pizzas == null || pizzas.length == 0) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.EMPTY_ORDER);
        }

        // Check if max pizza count exceeded
        if (pizzas.length > 4) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
        }

        // Check if pizzas are from single restaurant
        if (!orderValidationUtil.allPizzaFromSingleRestaurant(pizzas)) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }

        // Check if total price is correct
        if (!orderValidationUtil.isTotalPriceCorrect(pizzas, priceTotalInPence)) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);
        }

        // Check if credit card information is valid (invalid card number)
        if (!cardNumber.matches("\\d{16}")) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.CARD_NUMBER_INVALID);
        }

        // Check if credit card information is valid (invalid expiry date)
        if (!orderValidationUtil.isValidExpiryDate(expiryDate)) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Check if credit card information is valid (invalid CVV)
        if (cvv.length() != 3) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.CVV_INVALID);
        }

        // Check if restaurant is closed on the order day
        DayOfWeek orderDayOfWeek = orderDate.getDayOfWeek();
        if (!orderValidationUtil.isRestaurantOpen(pizzas, orderDayOfWeek)) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.RESTAURANT_CLOSED);
        }

        // Check if price of pizza is invalid
        OrderValidationCode validationCode = orderValidationUtil.isValidPizza(pizzas);
        if (validationCode != OrderValidationCode.NO_ERROR) {
            return new OrderValidation(OrderStatus.INVALID, validationCode);
        }

        // Return valid order result if passed all checks
        return new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
    }
}
