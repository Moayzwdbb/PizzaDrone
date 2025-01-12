package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.*;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.util.CalcPathUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.ilp.pizzadrone.constant.SystemConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Calculate Path service
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CalcPathServiceTest {

    @Autowired
    private CalcPathUtils calcPathUtils;

    @Autowired
    private DistanceService distanceService;

    // Mock test data
    private final LngLat restaurantLocation = new LngLat(-3.20254147052765, 55.9432847375794);

    private final LngLat appletonTowerLocation = new LngLat(APPLETON_LNG, APPLETON_LAT);

    private final NamedRegion noFlyZone = new NamedRegion(
            "George Square Area",
            List.of(new LngLat(-3.19057881832123, 55.9440241257753),
                    new LngLat(-3.18998873233795, 55.9428465054091),
                    new LngLat(-3.1870973110199, 55.9432881172426),
                    new LngLat(-3.18768203258514, 55.9444777403937),
                    new LngLat(-3.19057881832123, 55.9440241257753)));
    private final List<NamedRegion> noFlyZones = List.of(noFlyZone);

    private final NamedRegion centralArea = new NamedRegion(
            "central",
            List.of(new LngLat(-3.192473, 55.946233),
                    new LngLat(-3.192473, 55.942617),
                    new LngLat(-3.184319, 55.942617),
                    new LngLat(-3.184319, 55.946233),
                    new LngLat(-3.192473, 55.946233)));


    /**
     * Test case for calculated path for the drone to fly using the valid order data
     * It checks if the fly path is not empty, starts at the restaurant and ends at Appleton Tower
     * It also checks if the fly path does not enter the no-fly zone and has less than the maximum number of moves
     */
    @Test
    public void testCalculatePath() {
        // Calculate the path using the valid order
        List<LngLat> flyPath = calcPathUtils.calculatePath(
                restaurantLocation, appletonTowerLocation, noFlyZones, centralArea);


        // Check if the fly path is not enter the no-fly zone
        for (LngLat point : flyPath) {
            boolean isInsideNoFlyZone = distanceService.isInRegionChecker(new IsInRegionRequest(point, noFlyZone));
            assertFalse(isInsideNoFlyZone, "Path should not enter the no-fly zone");
        }

        // Check if the fly path starts at the restaurant
        assertEquals(restaurantLocation, flyPath.getFirst());

        // Check if the last position of fly path close to Appleton Tower
        assertTrue(distanceService.isCloseChecker(new LngLatPairRequest(flyPath.getLast(), appletonTowerLocation)),
                "Last position of the path should be close to Appleton Tower");

        // Check if the fly path has more than the maximum number of moves
        assertTrue(flyPath.size() <= DRONE_MAX_MOVES, "Path exceeds the maximum allowed moves");
    }
}
