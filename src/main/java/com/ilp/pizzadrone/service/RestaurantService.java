package com.ilp.pizzadrone.service;


import com.ilp.pizzadrone.dto.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Service class to fetch restaurant list from the REST API
 */
@Service
public class RestaurantService {

    private static final String RESTAURANTS_URL = "https://ilp-rest-2024.azurewebsites.net/restaurants";
    private final RestTemplate restTemplate;

    /**
     * Constructor for the RestaurantService
     */
    public RestaurantService(RestTemplate restTemplate) {
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
}
