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
 * Test class for nextPosition endpoint returns the correct position.
 * and 200 OK status when given a valid data. Also invalid data (semantically and syntactically)
 * is tested to ensure the endpoint returns the 400 Bad Request status.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NextPositionPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /nextPosition endpoint with valid data
     * @throws Exception if the test fails
     */
    @Test
    public void testNextPositionWithValidData() throws Exception {
        // Test /nextPosition endpoint with valid data
        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"angle\": 90}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"lng\": -3.192473, \"lat\": 55.946383}"));
    }

    /**
     * Test /nextPosition endpoint with semantic error data
     * @throws Exception if the test fails
     */
    @Test
    public void testNextPositionWithSemanticError() throws Exception {
            // Test /nextPosition endpoint with invalid angle
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"angle\": 900}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Angle: " +
                            "Must be a valid compass direction or 999 for hovering."));
    }

    /**
     * Test /nextPosition endpoint with syntax error data
     * @throws Exception if the test fails
     */
    @Test
    public void testNextPositionWithSyntaxError() throws Exception {
            // Test /nextPosition endpoint with invalid angle
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startPosition\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"angle\": 90}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "start position is missing"));
    }

    /**
     * Test /nextPosition endpoint with hovering angle return same position
     * @throws Exception if the test fails
     */
    @Test
    public void testNextPositionWithHoveringAngle() throws Exception {
            // Test /nextPosition endpoint with hovering angle
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\": {\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "\"angle\": 999}"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"lng\": -3.192473, \"lat\": 55.946233}"));
    }

    /**
     * Test /nextPosition endpoint with empty body data
     * @throws Exception if the test fails
     */
    @Test
    public void testNextPositionWithEmptyBody() throws Exception {
            // Test /nextPosition endpoint with empty body
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "start position is missing"));
    }

    @Test
    public void testNextPositionWithInvalidStartPosition() throws Exception {
            // Test /nextPosition endpoint with invalid start position
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\": {\"lng\": -3.192473, \"lat\": 555.946233}," +
                                  "\"angle\": 90}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "Latitude or Longitude are missing or out of range"));
    }

    @Test
    public void testNextPositionWithMissingAngle() throws Exception {
            // Test /nextPosition endpoint with missing angle
            mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\": {\"lng\": -3.192473, \"lat\": 55.946233}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Angle: Angle is missing."));
    }
}
