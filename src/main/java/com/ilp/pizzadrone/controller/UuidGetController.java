package com.ilp.pizzadrone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for the UuidGetController
 * This controller is responsible for handling the requests to get the student id
 */
@RestController
public class UuidGetController {
    /**
     * Return student id as a string
     * @return student id
     */
    @GetMapping("/uuid")
    public String getStudentId() {
        return "s2328889";
    }
}

