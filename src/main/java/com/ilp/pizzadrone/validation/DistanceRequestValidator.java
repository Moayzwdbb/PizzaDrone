package com.ilp.pizzadrone.validation;


import com.ilp.pizzadrone.dto.Position;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.model.NextPositionRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

/**
 * This class is used to validate the request.
 *
 */
public class RequestValidator {
    // Check if position coordinates valid and provided
    public boolean isInvalidPosition(Position position) {
        return position.getLongitude() == null || position.getLatitude() == null ||
                position.getLongitude() < -180 || position.getLongitude() > 180 ||
                position.getLatitude() < -90 || position.getLatitude() > 90;
    }

    // Validation method for Positions
    public ResponseEntity<?> validateDistanceRequest(LngLatPairRequest request) {
        // Check if the positions are not null
        if (request.getPosition1() == null || request.getPosition2() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "one or both positions are missing");
        }

        // Check if the position coordinates are valid
        if (isInvalidPosition(request.getPosition1()) || isInvalidPosition(request.getPosition2())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude are missing or out of range");
        }

        // return null if validations are pass
        return null;
    }

    // Check if the nextPosition Request Body valid
    public ResponseEntity<?> validateNextPosRequest(NextPositionRequest request) {
        // Check if the start position is not null
        if (request.getStartPosition() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "start position is missing");
        }

        // Check if the start position coordinates are valid
        if (isInvalidPosition(request.getStartPosition())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude are missing or out of range");
        }

        // Check if the angle valid
        if (request.getAngle() == null) {
            return ResponseEntity.badRequest().body("Invalid Angle: Angle is missing.");
        } else if (request.getAngle() < 0 || request.getAngle() > 360) {
            return ResponseEntity.badRequest().body("Invalid Angle: must be between 0 and 360");
        }

        // Return null if request valid
        return null;
    }

    // Check if the region request valid
    public ResponseEntity<?> validateRegionRequest(IsInRegionRequest request) {
        // Check if the position is not null
        if (request.getPosition() == null) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "position is missing");
        }

        // Check if the region is not null
        if (request.getRegion() == null) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region is missing");
        }

        // Check if the region name is not null
        if (request.getRegion().getName() == null) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region name is missing");
        }

        // Check if the position vertices are valid in vertices list
        ArrayList<Position> vertices = request.getRegion().getVertices();
        if (vertices == null || vertices.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "region boundary vertices are missing");
        }

        // Ensure the region has enough distinct vertices to form a polygon
        if (!isClosedRegion(vertices)) {
            return ResponseEntity.badRequest().body("Invalid Region Data: " +
                    "insufficient distinct vertices to form a closed region");
        }

        // Check all the vertices and position are valid
        for (Position vertex : vertices) {
            if (isInvalidPosition(vertex)) {
                return ResponseEntity.badRequest().body("Invalid Vertices Data: " +
                        "Latitude or Longitude are missing or out of range");
            }
        }

        // Check if the position is valid
        if (isInvalidPosition(request.getPosition())) {
            return ResponseEntity.badRequest().body("Invalid Position Data: " +
                    "Latitude or Longitude of the position is out of range");
        }

        // Return null if request valid
        return null;
    }

    // Validate region is closed
    public boolean isClosedRegion(ArrayList<Position> vertices) {
        if (vertices.size() < 3) {
            // A polygon needs at least 3 vertices to be valid
            return false;
        }

        // Counter for different vertex
        int count = 0;

        // check not all points are collinear
        boolean hasNonCollinearPoints = false;

        for (int i = 2; i < vertices.size(); i++) {
            Position p1 = vertices.get(i - 2);
            Position p2 = vertices.get(i - 1);
            Position p3 = vertices.get(i);

            if (!areCollinear(p1, p2, p3)) {
                hasNonCollinearPoints = true;
                break; // If we find a set of non-collinear points, the region is valid
            }
        }

        for (int i = 1; i < vertices.size(); i++) {
            Double prevLng = vertices.get(i - 1).getLongitude();
            Double prevLat = vertices.get(i - 1).getLatitude();

            Double currLng = vertices.get(i).getLongitude();
            Double currLat = vertices.get(i).getLatitude();

            // If the current vertex is distinct from the previous one, increment the count
            if (!(Double.compare(prevLng, currLng) == 0 && Double.compare(prevLat, currLat) == 0)) {
                count++;
            }
        }
        // If distinct vertex >= 3, the region is closed
        return count >= 2 && hasNonCollinearPoints;
    }


    // Helper method to check if three points are collinear
    private boolean areCollinear(Position p1, Position p2, Position p3) {
        double area = (p1.getLongitude() * (p2.getLatitude() - p3.getLatitude())) +
                (p2.getLongitude() * (p3.getLatitude() - p1.getLatitude())) +
                (p3.getLongitude() * (p1.getLatitude() - p2.getLatitude()));

        // If the area formed by three points is 0, they are collinear
        return Double.compare(area, 0.0) == 0;
    }
}
