package com.ilp.pizzadrone.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for GetController
 * This test verifies that the /uuid endpoint returns the correct student ID.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test /uuid endpoint returns the correct student ID
     * Verifies that the endpoint returns 200 OK status and the "s2328889" string
     * @throws Exception if the test fails
     */
    @Test
    public void testGetStudentId() throws Exception {
        mockMvc.perform(get("/uuid"))
                .andExpect(status().isOk())
                .andExpect(content().string("s2328889"));
    }
}
