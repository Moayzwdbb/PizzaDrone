package com.ilp.pizzadrone.model;

import com.ilp.pizzadrone.dto.LngLat;


/**
 * Represents a request containing two positions, each defined by longitude and latitude.
 * <p>
 * @param position1 the first geographic position (cannot be null)
 * @param position2 the second geographic position (cannot be null)
 */
public record LngLatPairRequest(LngLat position1, LngLat position2) {}