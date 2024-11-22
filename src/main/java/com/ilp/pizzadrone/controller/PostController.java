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

import static com.ilp.pizzadrone.constant.SystemConstants.DRONE_IS_CLOSE_DISTANCE;


/**
 * Controller class for handling POST requests
 * related to distance, position and region.
 */
@RestController
public class PostController {
    private final DistanceService distanceService;
    private final DistanceRequestValidator distanceRequestValidator;
    private final NextPosRequestValidator nextPosRequestValidator;
    private final RangeRequestValidator rangeRequestValidator;

    public PostController(DistanceService distanceService,
                          DistanceRequestValidator distanceRequestValidator,
                          NextPosRequestValidator nextPosRequestValidator,
                          RangeRequestValidator rangeRequestValidator) {
        this.distanceService = distanceService;
        this.distanceRequestValidator = distanceRequestValidator;
        this.nextPosRequestValidator = nextPosRequestValidator;
        this.rangeRequestValidator = rangeRequestValidator;
    }

    // Return distance between two positions
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

    // Checking if two positions are close
    @PostMapping("/isCloseTo")
    public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request) {
        // Validate the Positions
        ResponseEntity<?> validationResponse = distanceRequestValidator.validateDistanceRequest(request);

        // Return bad request if validation fails
        if (validationResponse != null) {
            return validationResponse;
        }

        // If positions are valid, determine is close
        boolean isClose = distanceService.calcEuclidDist(request) < DRONE_IS_CLOSE_DISTANCE;
        return ResponseEntity.ok(isClose);
    }

    // Return next position
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

    // Return true if given position in the region
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

