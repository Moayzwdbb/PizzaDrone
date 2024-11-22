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

    CompassDirection(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }


    /**
     * Get CompassDirection by angle.
     * @param angle the angle to look up
     * @return CompassDirection corresponding to the angle
     * @throws IllegalArgumentException if no matching direction is found
     */
    public static CompassDirection fromAngle(double angle) {
        for (CompassDirection direction : values()) {
            if (Double.compare(direction.angle, angle) == 0) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Invalid angle: " + angle);
    }

}
