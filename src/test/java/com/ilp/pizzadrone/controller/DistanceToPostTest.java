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
 * This test verifies that the /distanceTo endpoint returns the correct distance
 * and 200 OK status when given valid data. Also invalid data (semantically and syntactically)
 * is tested to ensure the endpoint returns the 400 Bad Request status.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DistanceToPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /distanceTo endpoint with valid data
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToWithValidData() throws Exception {
        // Test /distanceTo endpoint with valid data
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.942617}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.003616000000000952"));
    }

    /**
     * Test /distanceTo endpoint with semantic error data
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToWithSemanticError() throws Exception {
            // Test /distanceTo endpoint with same positions
            mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -300.192473, \"lat\": 550.946233}," +
                                  "\"position2\": {\"lng\": -3202.192473, \"lat\": 5533.946233}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "Latitude or Longitude are missing or out of range"));
    }

    /**
     * Test /distanceTo endpoint with syntax error data
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToWithSyntaxError() throws Exception {
        // Test /distanceTo endpoint with invalid data (null instead of number)
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat_Pos2\": 55.946233}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Position Data: " +
                        "Latitude or Longitude are missing or out of range"));
    }

    /**
     * Test /distanceTo endpoint with empty body
     * @throws Exception if the test fails
     */
    @Test
    public void testDistanceToWithEmptyBody() throws Exception {
        // Test /distanceTo endpoint with empty body
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Position Data: " +
                        "one or both positions are missing"));
    }
}
