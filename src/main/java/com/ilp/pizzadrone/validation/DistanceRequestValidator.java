package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.model.LngLatPairRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.ilp.pizzadrone.util.PositionValidationUtils.isInvalidPosition;

/**
 * This class is used to validate the request.
 * It checks if the position coordinates are valid and provided.
 */
@Component
public class DistanceRequestValidator {

    // Validation method for Positions
    public ResponseEntity<?> validateDistanceRequest(LngLatPairRequest request) {
        // Check if the positions are not null
        if (request.position1() == null || request.position2() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "one or both positions are missing");
        }

        // Check if the position coordinates are valid
        if (isInvalidPosition(request.position1()) || isInvalidPosition(request.position2())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude are missing or out of range");
        }

        // return null if validations are pass
        return null;
    }

}
