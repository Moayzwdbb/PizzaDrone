package com.ilp.pizzadrone.service;

import com.ilp.pizzadrone.dto.LngLat;
import com.mapbox.geojson.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Service class to convert a list of LngLat path to GeoJSON format using mapbox library
 */
@Service
public class GeoJsonService {
    /**
     * Convert a list of LngLat path to GeoJSON format using mapbox library
     *
     * @param flightPath the flight path as a list of LngLat
     * @return a map representing the GeoJSON structure
     */
    public String convertToGeoJson(List<LngLat> flightPath) {
        // Create features list
        List<Feature> features = new ArrayList<>();

        // Add flight path feature
        features.add(createFlightPathFeature(flightPath));

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
}
