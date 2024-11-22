package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.dto.Position;

import java.awt.geom.Path2D;
import java.util.List;

/**
 * Utility class for region.
 */
public class GeometryUtils {
    private static final double TOLERANCE = 1e-6;

    /**
     * Check if position is on the border between vertex1 and vertex2.
     */
    public static boolean isPointOnBorder(Position vertex1, Position vertex2, Double lng, Double lat) {
        double minLng = Math.min(vertex1.getLongitude(), vertex2.getLongitude());
        double maxLng = Math.max(vertex1.getLongitude(), vertex2.getLongitude());
        double minLat = Math.min(vertex1.getLatitude(), vertex2.getLatitude());
        double maxLat = Math.max(vertex1.getLatitude(), vertex2.getLatitude());

        // Check if the point lies within the bounds of the border
        if (lng >= minLng && lng <= maxLng && lat >= minLat && lat <= maxLat) {
            // Calculate the cross-product to check collinear
            double crossProduct = (lat - vertex1.getLatitude()) * (vertex2.getLongitude() - vertex1.getLongitude()) -
                    (lng - vertex1.getLongitude()) * (vertex2.getLatitude() - vertex1.getLatitude());
            return Math.abs(crossProduct) < TOLERANCE; // prevent for floating-point precision errors
        }

        return false;
    }

    /**
     * Check if the position is inside the polygon formed by vertices.
     */
    public static boolean isPointInRegion(List<Position> vertices, Position position) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("Vertices list cannot be null or empty");
        }

        // Create a Path2D object to represent the region
        Path2D region = createRegionPath(vertices);

        return region.contains(position.getLongitude(), position.getLatitude());
    }

    /**
     * Helper method to create a closed Path2D region from a list of vertices.
     */
    private static Path2D createRegionPath(List<Position> vertices) {
        Path2D region = new Path2D.Double();
        region.moveTo(vertices.getFirst().getLongitude(), vertices.getFirst().getLatitude());

        for (int i = 1; i < vertices.size(); i++) {
            region.lineTo(vertices.get(i).getLongitude(), vertices.get(i).getLatitude());
        }

        region.closePath();
        return region;
    }
}
