package com.ilp.pizzadrone.constant;

/**
 * This enum class is used to store compass direction angles.
 * Drones can only fly in one of the 16 major compass,
 * We use the convention that 0 means go East, 90 means go North, 180 means go West,
 * and 270 means go South, with the secondary and tertiary compass directions representing the
 * obvious directions between these four major compass directions.
 */
public enum CompassDirection {
    EAST(0),
    EAST_NORTH_EAST(22.5),
    NORTHEAST(45),
    NORTH_NORTH_EAST(67.5),
    NORTH(90),
    NORTH_NORTH_WEST(112.5),
    NORTHWEST(135),
    WEST_NORTH_WEST(157.5),
    WEST(180),
    WEST_SOUTH_WEST(202.5),
    SOUTHWEST(225),
    SOUTH_SOUTH_WEST(247.5),
    SOUTH(270),
    SOUTH_SOUTH_EAST(292.5),
    SOUTHEAST(315),
    EAST_SOUTH_EAST(337.5),
    HOVERING(999);

    private final double angle;

    /**
     * Constructor for the CompassDirection enum class
     * @param angle the angle of the compass direction
     */
    CompassDirection(double angle) {
        this.angle = angle;
    }

    /**
     * Get the angle of the compass direction
     * @return the angle of the compass direction
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Get Fly Direction by angle.
     * @param angle the angle to look up
     * @return CompassDirection corresponding to the angle
     */
    public static CompassDirection getFlyDirection(double angle) {
        // Check if the angle is hovering
        if (angle == 999) {
            return HOVERING;
        }

        // Check if the angle is valid
        if (angle > 360 || angle < 0) {
            throw new IllegalArgumentException();
        }

        CompassDirection closestDirection = null;
        double smallestDifference = Double.MAX_VALUE;

        // Find the closest compass direction to the angle
        for (CompassDirection direction : CompassDirection.values()) {
            if (direction == HOVERING) continue;

            double difference = Math.abs(direction.getAngle() - angle);

            if (difference > 180) {
                difference = 360 - difference;
            }

            if (difference < smallestDifference) {
                smallestDifference = difference;
                closestDirection = direction;
            }
        }

        return closestDirection;
    }

}
