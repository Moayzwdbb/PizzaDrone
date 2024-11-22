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
 * Test class for isInRegion endpoint returns True if a position is in a region.
 * and 200 OK status when given a valid data. Also invalid data (semantically and syntactically)
 * is tested to ensure the endpoint returns the 400 Bad Request status.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IsInNamedRegionPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /isInRegion endpoint returns True if a position is in a region.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithValidData() throws Exception {
        // Test /isInRegion endpoint with valid data
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -3.186000, \"lat\": 55.94400}," +
                                  "\"region\": {\"name\": \"central\", \"vertices\": " +
                                  "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Test /isInRegion endpoint return False when position is outside the region.
     * @throws Exception if the test fails
     */
    @Test
    public void testNotInRegionWithValidData() throws Exception {
        // Test /isInRegion endpoint with valid data
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -3.186000, \"lat\": 70.94400}," +
                                "\"region\": {\"name\": \"central\", \"vertices\": " +
                                "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Test /isInRegion endpoint with semantic error data.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithSemanticError() throws Exception {
            // Test /isInRegion endpoint with invalid region
            mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -390.186000, \"lat\": 550.94400}," +
                                "\"region\": {\"name\": \"central\", \"vertices\": " +
                                "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Position Data: " +
                            "Latitude or Longitude of the position is out of range"));
    }

    /**
     * Test /isInRegion endpoint with missing region data.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithMissingRegion() throws Exception {
        // Test /isInRegion endpoint with syntax error
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": 1.234, \"lat\": 1.222}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Region Data: " +
                        "region is missing"));
    }

    /**
     * Test /isInRegion endpoint with syntax error data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithMissingRegionName() throws Exception {
            // Test /isInRegion endpoint with syntax error
            mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": 1.234, \"lat\": 1.222}," +
                                  "\"region\": {\"names\": \"central\", \"verticesList\": " +
                                  "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid Region Data: " +
                            "region name is missing"));
    }

    /**
     * Test /isInRegion endpoint with invalid vertex data.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithInvalidVertex() throws Exception {
        // Test /isInRegion endpoint with valid data
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -3.186000, \"lat\": 55.94400}," +
                                "\"region\": {\"name\": \"central\", \"vertices\": " +
                                "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                "{\"lng\": -333.192473, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Vertices Data: " +
                        "Latitude or Longitude are missing or out of range"));
    }

    /**
     * Test /isInRegion endpoint with missing vertices' data.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithMissingVertices() throws Exception {
        // Test /isInRegion endpoint with syntax error
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": 1.234, \"lat\": 1.222}," +
                                  "\"region\": {\"name\": \"central\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Region Data: " +
                        "region boundary vertices are missing"));
    }

    /**
     * Test /isInRegion endpoint with insufficient vertices' data.
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithInsufficientVertices() throws Exception {
        // Test /isInRegion endpoint with valid data
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -3.186000, \"lat\": 55.94400}," +
                                "\"region\": {\"name\": \"central\", \"vertices\": " +
                                "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Region Data: " +
                        "insufficient distinct vertices to form a closed region"));
    }

    /**
     * Test /isInRegion endpoint with empty body data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithEmptyBody() throws Exception {
            // Test /isInRegion endpoint with empty body
            mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                    .andExpect(status().isBadRequest());
    }
}
