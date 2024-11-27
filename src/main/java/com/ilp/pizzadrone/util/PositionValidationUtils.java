package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.constant.CompassDirection;
import com.ilp.pizzadrone.dto.LngLat;


/**
 * // Utility class for validation
 */
public class PositionValidationUtils {
    // check if a position's coordinates are valid
    public static boolean isInvalidPosition(LngLat position) {
        return position.lng() == null || position.lat() == null ||
                position.lng() < -180 || position.lat() > 180 ||
                position.lng() < -90 || position.lat() > 90;
    }


    // check if a next position angle is valid
    public static boolean isValidAngle(Double angle) {
        // Check if angle matches one of the CompassDirection angles
        try {
            CompassDirection.getFlyDirection(angle);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
