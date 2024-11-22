package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.dto.LngLat;

import java.awt.geom.Path2D;
import java.util.List;

/**
 * Utility class for handling region operations such as checking if a point is inside a region or on its border.
 */
public class RegionUtils {
    private static final double TOLERANCE = 1e-6;

    /**
     * Validates if a region is closed.
     * A closed region requires at least three distinct and non-collinear points.
     * @param vertices list of vertices
     */
    public static boolean isClosedRegion(List<LngLat> vertices) {
        if (vertices.size() < 3) {
            // A polygon needs at least 3 vertices to be valid
            return false;
        }

        // Counter for different vertex
        int count = 0;

        // check not all points are collinear
        boolean hasNonCollinearPoints = false;

        for (int i = 2; i < vertices.size(); i++) {
            LngLat p1 = vertices.get(i - 2);
            LngLat p2 = vertices.get(i - 1);
            LngLat p3 = vertices.get(i);

            if (!areCollinear(p1, p2, p3)) {
                hasNonCollinearPoints = true;
                break; // If we find a set of non-collinear points, the region is valid
            }
        }

        for (int i = 1; i < vertices.size(); i++) {
            Double prevLng = vertices.get(i - 1).lng();
            Double prevLat = vertices.get(i - 1).lat();

            Double currLng = vertices.get(i).lng();
            Double currLat = vertices.get(i).lat();

            // If the current vertex is distinct from the previous one, increment the count
            if (!(Double.compare(prevLng, currLng) == 0 && Double.compare(prevLat, currLat) == 0)) {
                count++;
            }
        }
        // If distinct vertex >= 3, the region is closed
        return count >= 2 && hasNonCollinearPoints;
    }


    /**
     * Helper method to check if three points are collinear.
     * @param p1 first position
     * @param p2 second position
     * @param p3 third position
     */
    private static boolean areCollinear(LngLat p1, LngLat p2, LngLat p3) {
        double area = (p1.lng() * (p2.lat() - p3.lat())) +
                (p2.lng() * (p3.lat() - p1.lat())) +
                (p3.lng() * (p1.lat() - p2.lat()));

        // If the area formed by three points is 0, they are collinear
        return Double.compare(area, 0.0) == 0;
    }

    /**
     * Check if position is on the border between vertex1 and vertex2.
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @param lng longitude of the position
     * @param lat latitude of the position
     */
    public static boolean isPointOnBorder(LngLat vertex1, LngLat vertex2, Double lng, Double lat) {
        double minLng = Math.min(vertex1.lng(), vertex2.lng());
        double maxLng = Math.max(vertex1.lng(), vertex2.lng());
        double minLat = Math.min(vertex1.lat(), vertex2.lat());
        double maxLat = Math.max(vertex1.lat(), vertex2.lat());

        // Check if the point lies within the bounds of the border
        if (lng >= minLng && lng <= maxLng && lat >= minLat && lat <= maxLat) {
            // Calculate the cross-product to check collinear
            double crossProduct = (lat - vertex1.lat()) * (vertex2.lng() - vertex1.lng()) -
                    (lng - vertex1.lng()) * (vertex2.lat() - vertex1.lng());
            return Math.abs(crossProduct) < TOLERANCE; // prevent for floating-point precision errors
        }

        return false;
    }

    /**
     * Check if the position is inside the polygon formed by vertices.
     * @param vertices list of vertices
     * @param position position to check
     */
    public static boolean isPointInRegion(List<LngLat> vertices, LngLat position) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("Vertices list cannot be null or empty");
        }

        // Create a Path2D object to represent the region
        Path2D region = createRegionPath(vertices);

        return region.contains(position.lng(), position.lat());
    }

    /**
     * Helper method to create a closed Path2D region from a list of vertices.
     * @param vertices list of vertices
     */
    private static Path2D createRegionPath(List<LngLat> vertices) {
        Path2D region = new Path2D.Double();
        region.moveTo(vertices.getFirst().lng(), vertices.getFirst().lat());

        for (int i = 1; i < vertices.size(); i++) {
            region.lineTo(vertices.get(i).lng(), vertices.get(i).lat());
        }

        region.closePath();
        return region;
    }
}
