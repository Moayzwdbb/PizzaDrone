package com.ilp.pizzadrone.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for validating orders
 * retrieve a list of orders including an orderStatus and
 * orderValidationCode, validate the order and assert that the
 * validation result matches the expected order status and order validation code.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ValidateOrderServiceTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /validateOrder endpoint return 200 OK status with valid data
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrder() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 4900," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}," + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"VALID\"," +
                        " \"orderValidationCode\": \"NO_ERROR\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with empty pizza
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithEmptyPizza() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"EMPTY_ORDER\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with max pizza count exceeded
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithExceededPizza() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}," + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}," + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"MAX_PIZZA_COUNT_EXCEEDED\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with not defined pizza
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithNotDefinedPizza() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: NotDefined\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"NotDefined\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"PIZZA_NOT_DEFINED\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with invalid pizza price
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithInvalidPrice() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1001" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"PRICE_FOR_PIZZA_INVALID\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with invalid expiry date
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithInvalidExpiryData() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/24\"," + // Invalid expiry date
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"EXPIRY_DATE_INVALID\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with different pizza restaurant
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithUndefinedPizza() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R2: Meat Lover\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"PIZZA_FROM_MULTIPLE_RESTAURANTS\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with total price incorrect
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithTotalIncorrect() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2600," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"TOTAL_INCORRECT\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with incorrect cvv
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithIncorrectCVV() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"abc\"" + // incorrect cvv
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"CVV_INVALID\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with incorrect card number
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithIncorrectCardNumber() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"a417276782765083\"," + // incorrect card number
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"CARD_NUMBER_INVALID\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with closed day order
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrderWithClosedDay() throws Exception {
        // Closed day is Wednesday and Thursday in R1
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-12-18\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" + "{" +
                                "\"name\": \"R1: Margarita\"," +
                                "\"priceInPence\": 1000" +
                                "}," + "{" +
                                "\"name\": \"R1: Calzone\"," +
                                "\"priceInPence\": 1400" +
                                "}" + "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"RESTAURANT_CLOSED\"}"));
    }

}
