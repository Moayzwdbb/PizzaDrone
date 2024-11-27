package com.ilp.pizzadrone.service;


import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.dto.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Service class to fetch data from the REST API.
 * Includes methods for fetching restaurants, no-fly zones and the central area.
 */
@Service
public class RetrieveAPIService {

    private static final String RESTAURANTS_URL = "https://ilp-rest-2024.azurewebsites.net/restaurants";
    private static final String NO_FLY_ZONES_URL = "https://ilp-rest-2024.azurewebsites.net/noFlyZones";
    private static final String CENTRAL_AREA_URL = "https://ilp-rest-2024.azurewebsites.net/centralArea";
    private final RestTemplate restTemplate;

    /**
     * Constructor for the RestaurantService
     */
    public RetrieveAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the list of restaurants from the REST API
     *
     * @return the list of restaurants
     */
    public List<Restaurant> fetchRestaurants() {
        Restaurant[] restaurants = restTemplate.getForObject(RESTAURANTS_URL, Restaurant[].class);
        assert restaurants != null : "The fetched restaurants should not be null";
        return List.of(restaurants);
    }

    /**
     * Fetches the list of no-fly zones from the REST API
     *
     * @return the list of no-fly zones
     */
    public List<NamedRegion> fetchNoFlyZones() {
        NamedRegion[] noFlyZones = restTemplate.getForObject(NO_FLY_ZONES_URL, NamedRegion[].class);
        assert noFlyZones != null : "The fetched no-fly zones should not be null";
        return List.of(noFlyZones);
    }

    /**
     * Fetches the central area from the REST API
     *
     * @return the central area
     */
    public NamedRegion fetchCentralArea() {
        NamedRegion centralArea = restTemplate.getForObject(CENTRAL_AREA_URL, NamedRegion.class);
        assert centralArea != null : "The fetched central area should not be null";
        return centralArea;
    }
}
