package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.constant.CompassDirection;
import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.model.NextPositionRequest;
import com.ilp.pizzadrone.util.RegionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ilp.pizzadrone.constant.SystemConstants.DRONE_IS_CLOSE_DISTANCE;
import static com.ilp.pizzadrone.constant.SystemConstants.DRONE_MOVE_DISTANCE;

/**
 * Service class for calculating distances and positions
 * between two points and checking if a point is inside a region
 * or on its boundary.
 */
@Service
public class DistanceService {
    /**
     * Calculate the Euclidean distance between two points
     * @param request the request containing the two points
     * @return the Euclidean distance between the two points
     */
    public Double calcEuclidDist(LngLatPairRequest request) {
        Double position1Lng = request.position1().lng();
        Double position1Lat = request.position1().lat();
        Double position2Lng = request.position2().lng();
        Double position2Lat = request.position2().lat();

        return Math.sqrt(Math.pow(position1Lng - position2Lng, 2) + Math.pow(position1Lat - position2Lat, 2));
    }

    /**
     * Check if two positions are close to each other
     * @param request the request containing the two positions
     * @return true if the two positions are close to each other, false otherwise
     */
    public boolean isCloseChecker(LngLatPairRequest request) {
        return calcEuclidDist(request) < DRONE_IS_CLOSE_DISTANCE;
    }


    /**
     * Calculate the next position of the drone
     * @param request the request containing the current position and the angle
     * @return the next position of the drone
     */
    public LngLat calcNextPosition(NextPositionRequest request) {
        double startLng = request.start().lng();
        double startLat = request.start().lat();
        CompassDirection direction = CompassDirection.getFlyDirection(request.angle());

        // Handle hovering
        if (direction == CompassDirection.HOVERING) {
            return new LngLat(startLng, startLat);
        }

        // Calculate radians
        double radians = Math.toRadians(direction.getAngle());

        // Calculate movement distances
        double lngDistance = DRONE_MOVE_DISTANCE * Math.cos(radians);
        double latDistance = DRONE_MOVE_DISTANCE * Math.sin(radians);

        // Calculate and return new position
        return new LngLat(startLng + lngDistance, startLat + latDistance);
    }

    /**
     * Check if a position is inside a region or on its boundary
     * @param request the request containing the position and the region
     * @return true if the position is inside the region or on its boundary, false otherwise
     */
    public boolean isInRegionChecker(IsInRegionRequest request) {
        LngLat position = request.position();
        List<LngLat> vertices = request.region().vertices();

        // Check if the point is inside the region or on its boundary
        if (RegionUtils.isPointInRegion(vertices, position)) {
            return true;
        }

        // Check for border case
        for (int i = 0; i < vertices.size(); i++) {
            LngLat vertex1 = vertices.get(i);
            LngLat vertex2 = vertices.get((i + 1) % vertices.size());
            if (RegionUtils.isPointOnBorder(vertex1, vertex2, position.lng(), position.lat())) {
                return true;
            }
        }

        // Otherwise, the position is not within the region
        return false;
    }
}