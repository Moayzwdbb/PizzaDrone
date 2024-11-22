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
 * Test class for the PostController class.
 * This test verifies that the /distanceTo endpoint returns the correct distance
 * and 200 OK status when given valid data. Also invalid data (semantically and syntactically)
 * is tested to ensure the endpoint returns the 400 Bad Request status.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PostDistanceToTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /distanceTo endpoint with valid data
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToValidData() throws Exception {
        // Test /distanceTo endpoint with valid data
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.942617}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.003616000000000952"));
    }

    /**
     * Test /distanceTo endpoint with valid data of same positions
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToSamePosition() throws Exception {
            // Test /distanceTo endpoint with same positions
            mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));

    }

    /**
     * Test /distanceTo endpoint with invalid data (latitude out of range)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataLatitudeOutOfRange() throws Exception {
        // Test /distanceTo endpoint with invalid data (latitude out of range)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 91.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /distanceTo endpoint with invalid data (longitude out of range)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataLongitudeOutOfRange() throws Exception {
        // Test /distanceTo endpoint with invalid data (longitude out of range)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -181.192473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /distanceTo endpoint with invalid data (missing position)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataMissingPosition1() throws Exception {
        // Test /distanceTo endpoint with invalid data (missing position1)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /distanceTo endpoint with invalid data (invalid string)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataStringInsteadOfNumber() throws Exception {
        // Test /distanceTo endpoint with invalid data (string instead of number)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": \"invalid\"}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /distanceTo endpoint with invalid data (missing value)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataMissingValue() throws Exception {
        // Test /distanceTo endpoint with invalid data (missing value)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test /distanceTo endpoint with invalid data (null instead of number)
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToInvalidDataNullInsteadOfNumber() throws Exception {
        // Test /distanceTo endpoint with invalid data (null instead of number)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": null}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                .andExpect(status().isBadRequest());
    }
}
