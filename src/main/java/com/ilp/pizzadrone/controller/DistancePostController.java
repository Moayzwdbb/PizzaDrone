package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.model.NextPositionRequest;
import com.ilp.pizzadrone.service.DistanceService;
import com.ilp.pizzadrone.validation.DistanceRequestValidator;
import com.ilp.pizzadrone.validation.NextPosRequestValidator;
import com.ilp.pizzadrone.validation.RangeRequestValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller class for handling POST requests
 * related to distance, position and region.
 */
@RestController
public class DistancePostController {
    private final DistanceService distanceService;
    private final DistanceRequestValidator distanceRequestValidator;
    private final NextPosRequestValidator nextPosRequestValidator;
    private final RangeRequestValidator rangeRequestValidator;

    /**
     * Constructor for PositionsPostController
     *
     * @param distanceService          Service for calculating distances
     * @param distanceRequestValidator Validator for distance requests
     * @param nextPosRequestValidator  Validator for next position requests
     * @param rangeRequestValidator    Validator for region requests
     */
    public DistancePostController(DistanceService distanceService,
                                  DistanceRequestValidator distanceRequestValidator,
                                  NextPosRequestValidator nextPosRequestValidator,
                                  RangeRequestValidator rangeRequestValidator) {
        this.distanceService = distanceService;
        this.distanceRequestValidator = distanceRequestValidator;
        this.nextPosRequestValidator = nextPosRequestValidator;
        this.rangeRequestValidator = rangeRequestValidator;
    }

    /**
     * Calculate the distance between two positions
     *
     * @param request Request containing two positions
     * @return ResponseEntity containing the distance
     */
    @PostMapping("/distanceTo")
    public ResponseEntity<?> distanceTo(@RequestBody LngLatPairRequest request) {
        // Validate the Positions

        ResponseEntity<?> validationResponse = distanceRequestValidator.validateDistanceRequest(request);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        // If positions are valid, calculate the distance
        double distance = distanceService.calcEuclidDist(request);
        return ResponseEntity.ok(distance);
    }

    /**
     * Check if two positions are close to each other
     *
     * @param request Request containing two positions
     * @return ResponseEntity containing the result
     */
    @PostMapping("/isCloseTo")
    public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request) {
        // Validate the Positions
        ResponseEntity<?> validationResponse = distanceRequestValidator.validateDistanceRequest(request);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        // If positions are valid, determine is close
        boolean isClose = distanceService.isCloseChecker(request);
        return ResponseEntity.ok(isClose);
    }

    /**
     * Calculate the next position based on the current position and angle
     *
     * @param request Request containing the current position and angle
     * @return ResponseEntity containing the next position
     */
    @PostMapping("/nextPosition")
    public ResponseEntity<?> nextPosition(@RequestBody NextPositionRequest request) {
        // Validate request
        ResponseEntity<?> validationResponse = nextPosRequestValidator.validateNextPosRequest(request);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        LngLat destination = distanceService.calcNextPosition(request);
        return ResponseEntity.ok(destination);
    }

    /**
     * Check if a position is within a region
     *
     * @param request Request containing the position
     * @return ResponseEntity containing the result
     */
    @PostMapping("/isInRegion")
    public ResponseEntity<?> isInRegion(@RequestBody IsInRegionRequest request) {
        // Validate request
        ResponseEntity<?> validationResponse = rangeRequestValidator.validateRegionRequest(request);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        boolean isInRegionRes = distanceService.isInRegionChecker(request);
        return ResponseEntity.ok(isInRegionRes);
    }
}

