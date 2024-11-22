package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.model.NextPositionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.ilp.pizzadrone.util.ValidationUtil.isInvalidPosition;
import static com.ilp.pizzadrone.util.ValidationUtil.isValidAngle;


/**
 * This class is used to validate the request.
 * It checks if the position coordinates and angle are valid and provided in nextPosition request.
 */
@Component
public class NextPosRequestValidator {
    /**
     * Validate the next position request
     * @param request the next position request
     * @return ResponseEntity with error message if request is invalid, null if request is valid
     */
    public ResponseEntity<?> validateNextPosRequest(NextPositionRequest request) {
        // Check if the start position is not null
        if (request.start() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "start position is missing");
        }

        // Check if the start position coordinates are valid
        if (isInvalidPosition(request.start())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude are missing or out of range");
        }

        // Check if the angle is missing
        if (request.angle() == null) {
            return ResponseEntity.badRequest().body("Invalid Angle: Angle is missing.");
        }

        // Check if the angle is valid
        if (!isValidAngle(request.angle())) {
            return ResponseEntity.badRequest().body("Invalid Angle: " +
                    "Must be a valid compass direction or 999 for hovering.");
        }

        // Return null if request valid
        return null;
    }
}
