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
 * Test class for isCloseTo endpoint returns True if two positions are close.
 * and 200 OK status when given a valid data. Also invalid data (semantically and syntactically)
 * is tested to ensure the endpoint returns the 400 Bad Request status.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IsCloseToPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /isCloseTo endpoint with valid data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsCloseToWithValidData() throws Exception {
        // Test /isCloseTo endpoint with valid data
        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946117}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Test /isCloseTo endpoint return false when two positions are not close
     * @throws Exception if the test fails
     */
    @Test
    public void testNotCloseToWithValidData() throws Exception {
        // Test /isCloseTo endpoint with valid data
        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.182473, \"lat\": 55.946233}," +
                                  "\"position2\": {\"lng\": -3.192473, \"lat\": 55.946117}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Test /isCloseTo endpoint with semantic error data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsCloseToWithSemanticError() throws Exception {
            // Test /isCloseTo endpoint with same positions
            mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3004.192473, \"lat\": 550.946233}," +
                                  "\"position2\": {\"lng\": -390.192473, \"lat\": 551.946233}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "Latitude or Longitude are missing or out of range"));
    }

    /**
     * Test /isCloseTo endpoint with syntax error data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsCloseToWithSyntaxError() throws Exception {
            // Test /isCloseTo endpoint with syntax error
            mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position1\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"position3\": {\"lng\": -3.192473, \"lat\": 55.946117}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "one or both positions are missing"));
    }

    /**
     * Test /isCloseTo endpoint with empty body data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsCloseToWithEmptyBody() throws Exception {
            // Test /isCloseTo endpoint with empty body
            mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "one or both positions are missing"));
    }
}
