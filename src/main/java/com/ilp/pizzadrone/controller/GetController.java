package com.ilp.pizzadrone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {
    /**
     * Return student id as a string
     * @return student id
     */
    @GetMapping("/uuid")
    public String getStudentId() {
        return "s2328889";
    }
}

