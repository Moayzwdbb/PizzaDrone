package com.ilp.pizzadrone.controller;

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
 * Test class for validating Bad Request scenarios in /calcDeliveryPath endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CalcPathPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /calcDeliveryPath endpoint with valid order and returns 200 OK.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidOrder() throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
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
                .andExpect(status().isOk());
    }

    /**
     * Test /calcDeliveryPath endpoint with empty body returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithEmptyBody() throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }

    /**
     * Test /calcDeliveryPath endpoint with invalid order returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidOrderWithInvalidField() throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"orderStatus\": \"VALID\"," +
                                "\"orderValidationCode\": \"NO_ERROR\", " +
                                "\"priceTotalInPence\": 2400," +
                                "\"pizzasInOrder\": null," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }

}
