package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.Order;
import com.ilp.pizzadrone.model.OrderValidation;
import com.ilp.pizzadrone.service.OrderService;
import com.ilp.pizzadrone.service.RetrieveAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class ValidateOrderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetrieveAPIService retrieveAPIService;

    @Autowired
    private OrderService orderService;

    /**
     * Test /validateOrder endpoint return 200 OK status with valid data
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrder() throws Exception {
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
                                "\"cvv\": \"989\"" +
                                "}}"
                                ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"VALID\"," +
                        " \"orderValidationCode\": \"NO_ERROR\"}"));
    }

    /**
     * Test /validateOrder endpoint return 200 OK status with invalid expiry date
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidateOrderWithInvalidExpiryData() throws Exception {
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
                                "\"creditCardExpiry\": \"00/00\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\": \"INVALID\"," +
                        " \"orderValidationCode\": \"EXPIRY_DATE_INVALID\"}"));
    }

    /**
     * Test /validateOrder endpoint return 400 bad request status with empty body
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidateOrderWithEmptyBody() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order Data: Order is invalid"));
    }

    /**
     * Test validateOrder with order list
     */
    @Test
    public void testValidateOrderWithOrderList() {
        // Fetch orders from the test service
        List<Order> testOrders = retrieveAPIService.fetchOrders();

        for (Order order : testOrders) {
            // Validate the order
            OrderValidation validation = orderService.validateOrder(order);

            // Assert that the validation result matches the expected order status
            assertEquals(order.getOrderStatus(), validation.orderStatus());
            assertEquals(order.getOrderValidationCode(), validation.orderValidationCode());
        }
    }
}
