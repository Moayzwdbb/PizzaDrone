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
 * Test class for validating Bad Request scenarios in /validateOrder endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ValidateOrderPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /validateOrder endpoint with empty body returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithEmptyBody() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }

    /**
     * Test /validateOrder endpoint with missing credit card information returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithMissingCreditCard() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": [" +
                                "{\"name\": \"R1: Margarita\", \"priceInPence\": 1000}," +
                                "{\"name\": \"R1: Calzone\", \"priceInPence\": 1400}" +
                                "]" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }

    /**
     * Test /validateOrder endpoint with negative total price returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithNegativeTotalPrice() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"priceTotalInPence\": -2500," +
                                "\"pizzasInOrder\": [" +
                                "{\"name\": \"R1: Margarita\", \"priceInPence\": 1000}," +
                                "{\"name\": \"R1: Calzone\", \"priceInPence\": 1400}" +
                                "]," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }

    /**
     * Test /validateOrder endpoint with malformed JSON returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithMalformedJson() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\", \"orderDate\": \"2024-11-23\"" + "}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /validateOrder endpoint with null pizza in order returns 400 Bad Request.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testValidateOrderWithNullPizza() throws Exception {
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\": \"26B2C04C\"," +
                                "\"orderDate\": \"2024-11-23\"," +
                                "\"priceTotalInPence\": 2500," +
                                "\"pizzasInOrder\": null," +
                                "\"creditCardInformation\": {" +
                                "\"creditCardNumber\": \"4172767827650837\"," +
                                "\"creditCardExpiry\": \"06/25\"," +
                                "\"cvv\": \"989\"" +
                                "}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Order: Order data is invalid"));
    }
}
