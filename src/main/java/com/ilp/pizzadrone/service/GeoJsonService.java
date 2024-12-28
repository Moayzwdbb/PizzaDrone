package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.dto.Restaurant;
import com.mapbox.geojson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LAT;
import static com.ilp.pizzadrone.constant.SystemConstants.APPLETON_LNG;
import static com.ilp.pizzadrone.constant.SystemConstants.CENTRAL_REGION_NAME;

/**
 * Service class to convert a list of LngLat path to GeoJSON format using mapbox library
 */
@Service
public class GeoJsonService {
    private final RetrieveAPIService retrieveAPIService;

    private List<Restaurant> restaurants;
    private NamedRegion centralArea;
    private List<NamedRegion> noFlyZones;


    /**
     * Constructor for the GeoJsonService class
     *
     * @param retrieveAPIService the RetrieveAPIService object
     */
    public GeoJsonService(RetrieveAPIService retrieveAPIService) {
        this.retrieveAPIService = retrieveAPIService;
    }



    /**
     * Convert a list of LngLat path to GeoJSON format using mapbox library
     *
     * @param flightPath the flight path as a list of LngLat
     * @return a map representing the GeoJSON structure
     */
    public String convertToGeoJson(List<LngLat> flightPath) {
        // Fetch and cache the data from the API
        if (restaurants == null) {
            restaurants = retrieveAPIService.fetchRestaurants();
        }
        if (centralArea == null) {
            centralArea = retrieveAPIService.fetchCentralArea();
        }
        if (noFlyZones == null) {
            noFlyZones = retrieveAPIService.fetchNoFlyZones();
        }

        // Create features list
        List<Feature> features = new ArrayList<>();

        // Add flight path feature
        features.add(createFlightPathFeature(flightPath));

        // Add Appleton Tower feature
        features.add(createFeatureFromPoint(Point.fromLngLat(APPLETON_LNG, APPLETON_LAT),
                "Appleton Tower", "#ffff00"));

        // Add central area feature
        features.add(convertVerticesToPolygon(centralArea, CENTRAL_REGION_NAME, "none"));

        // Add restaurant features
        for (Restaurant restaurant : restaurants) {
            features.add(createFeatureFromPoint(Point.fromLngLat(restaurant.location().lng(), restaurant.location().lat()),
                    restaurant.name(), "#0000ff"));
        }

        // Add no-fly zone features
        for (NamedRegion noFlyZone : noFlyZones) {
            features.add(convertVerticesToPolygon(noFlyZone, noFlyZone.name(), "#ff0000"));
        }

        // Create FeatureCollection
        return FeatureCollection.fromFeatures(features).toJson();
    }

    /**
     * Create a feature from a list of LngLat points representing the flight path
     * @param flightPath the flight path as a list of LngLat
     * @return the feature representing the flight path
     */
    private Feature createFlightPathFeature(List<LngLat> flightPath) {
        List<Point> points = new ArrayList<>();
        for (LngLat point : flightPath) {
            points.add(Point.fromLngLat(point.lng(), point.lat()));
        }
        LineString lineString = LineString.fromLngLats(points);
        Feature feature = Feature.fromGeometry(lineString);
        feature.addStringProperty("name", "Flight Path");
        feature.addStringProperty("color", "#ff0000");
        return feature;
    }

    /**
     * Convert a NamedRegion object to a GeoJSON polygon feature
     * @param region the NamedRegion object
     * @param name the name of the feature
     * @param fillColor the fill color of the feature
     * @return the GeoJSON polygon feature
     */
    private Feature convertVerticesToPolygon(NamedRegion region, String name, String fillColor) {
        List<List<Point>> coordinates = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        for (LngLat vertex : region.vertices()) {
            points.add(Point.fromLngLat(vertex.lng(), vertex.lat()));
        }
        coordinates.add(points);
        Polygon polygon = Polygon.fromLngLats(coordinates);
        Feature feature = Feature.fromGeometry(polygon);
        feature.addStringProperty("name", name);
        feature.addStringProperty("fill", fillColor);
        return feature;
    }

    /**
     * Create a feature from a Point object
     *
     * @param point       the Point object
     * @param name        the name of the feature
     * @param markerColor the marker color
     * @return the GeoJSON feature
     */
    private Feature createFeatureFromPoint(Point point, String name, String markerColor) {
        Feature feature = Feature.fromGeometry(point);
        feature.addStringProperty("name", name);
        feature.addStringProperty("marker-color", markerColor);
        feature.addStringProperty("marker-symbol", "building");
        return feature;
    }
}
