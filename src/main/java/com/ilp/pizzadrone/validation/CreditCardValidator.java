package com.ilp.pizzadrone.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Utility class for validating credit card details
 */
@Component
public class CreditCardValidator {

    /**
     * Checks if the credit card expiry date is valid.
     *
     * @param expiryDate the credit card expiry date
     * @return True if the credit card expiry date is valid, false otherwise
     */
    public boolean isValidExpiryDate(String expiryDate, LocalDate orderDate) {
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
        return expiry.isAfter(orderDate);
    }
}
