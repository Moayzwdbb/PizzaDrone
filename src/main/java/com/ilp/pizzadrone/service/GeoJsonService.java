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
        // Fetch the data from the API
        List<Restaurant> restaurants = retrieveAPIService.fetchRestaurants();
        NamedRegion centralArea = retrieveAPIService.fetchCentralArea();
        List<NamedRegion> noFlyZones = retrieveAPIService.fetchNoFlyZones();

        // Create features list
        List<Feature> features = new ArrayList<>();

        // Create flight path line string
        List<Point> flyPathPoints = new ArrayList<>();

        for (LngLat point : flightPath) {
            flyPathPoints.add(Point.fromLngLat(point.lng(), point.lat()));
        }

        LineString flyPathLineString = LineString.fromLngLats(flyPathPoints);
        Feature flyPathFeature = Feature.fromGeometry(flyPathLineString);
        flyPathFeature.addStringProperty("name", "Flight Path");
        flyPathFeature.addStringProperty("color", "#ff0000");
        features.add(flyPathFeature);


        // Create Appleton Tower feature
        Point appletonTowerPoint = Point.fromLngLat(APPLETON_LNG, APPLETON_LAT);
        Feature appletonTowerFeature = Feature.fromGeometry(appletonTowerPoint);
        appletonTowerFeature.addStringProperty("name", "Appleton Tower");
        appletonTowerFeature.addStringProperty("marker-symbol", "building");
        appletonTowerFeature.addStringProperty("marker-color", "#ffff00");

        features.add(appletonTowerFeature);

        // Create central area feature
        List<List<Point>> centralAreaCoordinates = new ArrayList<>();
        List<Point> centralAreaPoints = new ArrayList<>();

        for (LngLat vertex : centralArea.vertices()) {
            centralAreaPoints.add(Point.fromLngLat(vertex.lng(), vertex.lat()));
        }
        centralAreaCoordinates.add(centralAreaPoints);

        Polygon centralAreaPolygon = Polygon.fromLngLats(centralAreaCoordinates);
        Feature centralAreaFeature = Feature.fromGeometry(centralAreaPolygon);
        centralAreaFeature.addStringProperty("name", CENTRAL_REGION_NAME);
        centralAreaFeature.addStringProperty("fill", "none");

        features.add(centralAreaFeature);

        // Create restaurant features
        for (Restaurant restaurant : restaurants) {
            Point restaurantPoint = Point.fromLngLat(restaurant.location().lng(), restaurant.location().lat());
            Feature restaurantFeature = Feature.fromGeometry(restaurantPoint);
            restaurantFeature.addStringProperty("name", restaurant.name());
            restaurantFeature.addStringProperty("marker-color", "#0000ff");
            restaurantFeature.addStringProperty("marker-symbol", "building");
            features.add(restaurantFeature);
        }

        // Create no-fly zone features
        for (NamedRegion noFlyZone : noFlyZones) {
            List<List<Point>> noFlyZoneCoordinates = new ArrayList<>();
            List<Point> noFlyZonePoints = new ArrayList<>();

            for (LngLat vertex : noFlyZone.vertices()) {
                noFlyZonePoints.add(Point.fromLngLat(vertex.lng(), vertex.lat()));
            }

            noFlyZoneCoordinates.add(noFlyZonePoints);

            Polygon noFlyZonePolygon = Polygon.fromLngLats(noFlyZoneCoordinates);
            Feature noFlyZoneFeature = Feature.fromGeometry(noFlyZonePolygon);
            noFlyZoneFeature.addStringProperty("name", noFlyZone.name());
            noFlyZoneFeature.addStringProperty("fill", "#ff0000");
            features.add(noFlyZoneFeature);
        }

        // Create FeatureCollection
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);

        // Convert the FeatureCollection to JSON
        return featureCollection.toJson();
    }
}
