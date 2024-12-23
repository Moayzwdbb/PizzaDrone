package com.ilp.pizzadrone.controller;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.model.NextPositionRequest;
import com.ilp.pizzadrone.service.DistanceService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the DistanceService.
 * Verify the functionality of the Euclidean distance calculation,
 * next position calculation, and region checking.
 */
public class DistanceServiceTest {
    private final DistanceService distanceService = new DistanceService();

    /**
     * Test the correct calculation of the Euclidean distance between two positions.
     */
    @Test
    public void testCalcEuclidDistDifferentPositions() {
        LngLat position1 = new LngLat(-3.192473, 55.946233);
        LngLat position2 = new LngLat(-3.192473, 55.942617);

        LngLatPairRequest request = new LngLatPairRequest(position1, position2);
        double expected = 0.003616000000000952;

        assertEquals(expected, distanceService.calcEuclidDist(request));
    }

    /**
     * Test the correct calculation of the Euclidean distance between the same positions.
     */
    @Test
    public void testCalcEuclidDistSamePosition() {
        LngLat position = new LngLat(-3.192473, 55.946233);

        LngLatPairRequest request = new LngLatPairRequest(position, position);
        double expected = 0.0;

        assertEquals(expected, distanceService.calcEuclidDist(request));
    }

    /**
     * Test the correct calculation of the next position based on the angle and starting position.
     */
    @Test
    public void testCalcNextPosition() {
        LngLat startPosition = new LngLat(-3.192473, 55.946233);

        NextPositionRequest request = new NextPositionRequest(startPosition, 90.0);

        LngLat expected = new LngLat(-3.192473, 55.946383);
        LngLat result = distanceService.calcNextPosition(request);

        assertEquals(expected.lng(), result.lng(), 1e-6);
        assertEquals(expected.lat(), result.lat(), 1e-6);
    }

    /**
     * Test the correct calculation of extreme angles (0 and 270 degrees).
     */
    @Test
    public void testNextPositionExtremeAngles() {
        LngLat startPosition = new LngLat(-3.192473, 55.946233);

        // Test for 0 degrees (East)
        NextPositionRequest eastRequest = new NextPositionRequest(startPosition, 0.0);
        LngLat eastExpected = new LngLat(-3.192323, 55.946233);
        LngLat eastResult = distanceService.calcNextPosition(eastRequest);

        assertEquals(eastExpected.lng(), eastResult.lng(), 1e-6);
        assertEquals(eastExpected.lat(), eastResult.lat(), 1e-6);

        // Test for 270 degrees (South)
        NextPositionRequest southRequest = new NextPositionRequest(startPosition, 270.0);
        LngLat southExpected = new LngLat(-3.192473, 55.946083);
        LngLat southResult = distanceService.calcNextPosition(southRequest);

        assertEquals(southExpected.lng(), southResult.lng(), 1e-6);
        assertEquals(southExpected.lat(), southResult.lat(), 1e-6);
    }

    /**
     * Test next position remains same when hovering.
     */
    @Test
    public void testNextPositionWithHovering() {
        LngLat startPosition = new LngLat(-3.192473, 55.946233);

        NextPositionRequest request = new NextPositionRequest(startPosition, 999.0);

        LngLat expected = new LngLat(-3.192473, 55.946233);
        LngLat result = distanceService.calcNextPosition(request);

        assertEquals(expected.lng(), result.lng(), 1e-6);
        assertEquals(expected.lat(), result.lat(), 1e-6);
    }

    /**
     * Test the isInRegionChecker to verify if a point is inside a region.
     */
    @Test
    public void testIsInRegion() {
        LngLat position = new LngLat(-3.192473, 55.946233);
        NamedRegion namedRegion = new NamedRegion("central", List.of(
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)
        ));



        IsInRegionRequest request = new IsInRegionRequest(position, namedRegion);

        boolean isInRegion = distanceService.isInRegionChecker(request);

        assertTrue(isInRegion);
    }

    /**
     * Test the isInRegionChecker to verify if a point is outside a region.
     */
    @Test
    public void testIsNotInRegion() {
        LngLat position = new LngLat(-3.193, 55.950); // Outside the region.
        NamedRegion namedRegion = new NamedRegion("central", List.of(
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)
        ));

        IsInRegionRequest request = new IsInRegionRequest(position, namedRegion);

        boolean isInRegion = distanceService.isInRegionChecker(request);

        assertFalse(isInRegion);
    }

    /**
     * Test the isInRegion return true if a point is on the border of a region.
     */
    @Test
    public void testIsInRegionOnBorder() {
        LngLat position = new LngLat(-3.192473, 55.946233); // On the border
        NamedRegion namedRegion = new NamedRegion("central", List.of(
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)
        ));

        IsInRegionRequest request = new IsInRegionRequest(position, namedRegion);

        boolean isInRegion = distanceService.isInRegionChecker(request);

        assertTrue(isInRegion);
    }
}
