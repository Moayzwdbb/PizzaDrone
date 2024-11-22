package com.ilp.pizzadrone.model;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;

/**
 * Represents a request to check if a position is inside a specified region.
 *
 * @param position    the position to check, represented as a {@link LngLat}
 * @param region the region to check against, represented as a {@link NamedRegion}
 */
public record IsInRegionRequest(LngLat position, NamedRegion region) {
}
