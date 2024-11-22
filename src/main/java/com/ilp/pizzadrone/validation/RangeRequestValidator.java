package com.ilp.pizzadrone.validation;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.util.RegionUtils;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ilp.pizzadrone.util.ValidationUtil.isInvalidPosition;


/**
 * This class is used to validate the range request.
 * It checks if the position coordinates and region vertices are valid and provided in the range request.
 */
@Component
public class RangeRequestValidator {
    // Check if the region request valid
    public ResponseEntity<?> validateRegionRequest(IsInRegionRequest request) {
        // Check if the position is not null
        if (request.position() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "position is missing");
        }

        // Check if the region is not null
        if (request.region() == null) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region is missing");
        }

        // Check if the region name is not null
        if (request.region().name() == null) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region name is missing");
        }

        // Check if the position vertices are valid in vertices list
        List<LngLat> vertices = request.region().vertices();
        if (vertices == null || vertices.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region boundary vertices are missing");
        }

        // Ensure the region has enough distinct vertices to form a polygon
        if (!RegionUtils.isClosedRegion(vertices)) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "insufficient distinct vertices to form a closed region");
        }

        // Check all the vertices and position are valid
        for (LngLat vertex : vertices) {
            if (isInvalidPosition(vertex)) {
                return ResponseEntity.badRequest().body("Invalid Vertices Data: " +
                        "Latitude or Longitude are missing or out of range");
            }
        }

        // Check if the position is valid
        if (isInvalidPosition(request.position())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude of the position is out of range");
        }

        // Return null if request valid
        return null;
    }

}
