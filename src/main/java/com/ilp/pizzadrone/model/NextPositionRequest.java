package com.ilp.pizzadrone.model;

import com.ilp.pizzadrone.dto.LngLat;

/**
 * Represents a request to calculate the next position of a drone based on a starting position and an angle.
 *
 * @param start the starting position as a {@link LngLat} (cannot be null)
 * @param angle the angle in degrees (must be 16 compass direction, or 999 for hovering)
 */
public record NextPositionRequest(LngLat start, Double angle) {
}
