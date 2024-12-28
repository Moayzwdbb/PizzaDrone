package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.dto.Restaurant;
import com.mapbox.geojson.FeatureCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LAT;
import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LNG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for the GeoPathService
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GeoJsonServiceTest {

    @Autowired
    private GeoJsonService geoJsonService;

    @MockBean
    private RetrieveAPIService retrieveAPIService;

    private final LngLat appletonTowerLocation = new LngLat(APPLETON_LNG, APPLETON_LAT);
    private final LngLat restaurantLocation = new LngLat(-3.20254147052765, 55.9432847375794);
    private final NamedRegion centralArea = new NamedRegion(
            "Central Area",
            List.of(new LngLat(-3.192473, 55.946233),
                    new LngLat(-3.192473, 55.942617),
                    new LngLat(-3.184319, 55.942617),
                    new LngLat(-3.184319, 55.946233),
                    new LngLat(-3.192473, 55.946233)));

    private final NamedRegion noFlyZone = new NamedRegion(
            "George Square Area",
            List.of(new LngLat(-3.19057881832123, 55.9440241257753),
                    new LngLat(-3.18998873233795, 55.9428465054091),
                    new LngLat(-3.1870973110199, 55.9432881172426),
                    new LngLat(-3.18768203258514, 55.9444777403937),
                    new LngLat(-3.19057881832123, 55.9440241257753)));

    private final Restaurant restaurant = new Restaurant(
            "Test Restaurant",
            restaurantLocation,
            null,
            null);

    /**
     * Test the convertToGeoJson method returns the expected GeoJSON format for the flight path
     */
    @Test
    public void testConvertToGeoJson() {
        // Mock the API responses
        when(retrieveAPIService.fetchRestaurants()).thenReturn(List.of(restaurant));
        when(retrieveAPIService.fetchCentralArea()).thenReturn(centralArea);
        when(retrieveAPIService.fetchNoFlyZones()).thenReturn(List.of(noFlyZone));

        // Prepare the flight path
        List<LngLat> flightPath = List.of(restaurantLocation, appletonTowerLocation);

        // Convert to GeoJSON
        String geoJson = geoJsonService.convertToGeoJson(flightPath);

        // Parse the GeoJSON
        FeatureCollection featureCollection = FeatureCollection.fromJson(geoJson);

        // Validate the number of features
        assert featureCollection.features() != null;
        assertEquals(5, featureCollection.features().size(),
                "Feature count mismatch");

        // Validate flight path LineString
        assertTrue(featureCollection.features().stream()
                .anyMatch(f -> "Flight Path".equals(f.getStringProperty("name"))),
                "Flight path missing");

        // Validate Appleton Tower
        assertTrue(featureCollection.features().stream()
                .anyMatch(f -> "Appleton Tower".equals(f.getStringProperty("name"))),
                "Appleton Tower missing");

        // Validate central area polygon
        assertTrue(featureCollection.features().stream()
                .anyMatch(f -> "central".equals(f.getStringProperty("name"))),
                "Central area missing");

        // Validate no-fly zone polygon
        assertTrue(featureCollection.features().stream()
                .anyMatch(f -> "George Square Area".equals(f.getStringProperty("name"))),
                "No-fly zone missing");

        // Validate restaurant
        assertTrue(featureCollection.features().stream()
                .anyMatch(f -> "Test Restaurant".equals(f.getStringProperty("name"))),
                "Restaurant missing");
    }
}
