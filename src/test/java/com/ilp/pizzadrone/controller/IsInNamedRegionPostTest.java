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
public class IsInRegionPostTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /isInRegion endpoint with valid data
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
     * Test /isInRegion endpoint with semantic error data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithSemanticError() throws Exception {
            // Test /isInRegion endpoint with invalid region
            mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": {\"lng\": -390.186000, \"lat\": 550.94400}," +
                                  "\"region\": {\"name\": \"central\", \"vertices\": " +
                                  "[{\"lng\": -3.192473, \"lat\": 558.946233}," +
                                  "{\"lng\": -367.192473, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                    .andExpect(status().isBadRequest());
    }

    /**
     * Test /isInRegion endpoint with syntax error data
     * @throws Exception if the test fails
     */
    @Test
    public void testIsInRegionWithSyntaxError() throws Exception {
            // Test /isInRegion endpoint with syntax error
            mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPosition\": {\"lng\": 1.234, \"lat\": 1.222}," +
                                  "\"region\": {\"names\": \"central\", \"verticesList\": " +
                                  "[{\"lng\": -3.192473, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.942617}," +
                                  "{\"lng\": -3.184319, \"lat\": 55.946233}," +
                                  "{\"lng\": -3.192473, \"lat\": 55.946233}]}}"))
                    .andExpect(status().isBadRequest());
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
