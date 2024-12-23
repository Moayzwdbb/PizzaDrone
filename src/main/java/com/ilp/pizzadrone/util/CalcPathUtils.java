package com.ilp.pizzadrone.util;

import com.ilp.pizzadrone.constant.CompassDirection;
import com.ilp.pizzadrone.dto.LngLat;
import com.ilp.pizzadrone.dto.NamedRegion;
import com.ilp.pizzadrone.dto.Node;
import com.ilp.pizzadrone.model.IsInRegionRequest;
import com.ilp.pizzadrone.model.LngLatPairRequest;
import com.ilp.pizzadrone.model.NextPositionRequest;
import com.ilp.pizzadrone.service.DistanceService;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Utility class for calculating the path for the drone to fly
 */
@Component
public class CalcPathUtils {
    private final DistanceService distanceService;

    public CalcPathUtils(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    /**
     * Calculate the path for the drone to fly from the restaurant to Appleton Tower
     *
     * @param restaurantLocation    the location of the restaurant
     * @param appletonTowerLocation the location of Appleton Tower
     * @param noFlyZones            the list of no-fly zones
     * @param centralArea           the central area
     * @return the list of LngLat points representing the path
     */
    public List<LngLat> calculatePath(LngLat restaurantLocation,
                                      LngLat appletonTowerLocation,
                                      List<NamedRegion> noFlyZones,
                                      NamedRegion centralArea) {
        // Initialize fly path list
        List<LngLat> flyPath = new ArrayList<>();

        // Using priority queue to store the frontier nodes
        PriorityQueue<Node> frontier = new PriorityQueue<>();

        // Using map to store the visited nodes
        Map<LngLat, Node> allNodes = new HashMap<>();

        // Flag to check if the drone is inside the central area
        boolean insideCentralArea = false;

        // Add the restaurant location to the frontier
        Node startNode = new Node(restaurantLocation, 0,
                calcHeuristic(restaurantLocation, appletonTowerLocation), null);
        frontier.add(startNode);
        allNodes.put(restaurantLocation, startNode);

        Node goalNode = null;

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();
            LngLat currentPosition = currentNode.position();

            // if the drone entered the central area, update the flag
            if (!insideCentralArea && distanceService.isInRegionChecker(
                    new IsInRegionRequest(currentPosition, centralArea))) {
                insideCentralArea = true;
            }

            // If the drone reached Appleton Tower, stop searching
            if (distanceService.isCloseChecker(new LngLatPairRequest(currentPosition, appletonTowerLocation))) {
                goalNode = new Node(appletonTowerLocation, currentNode.g(), 0, currentNode);
                break;
            }

            // Expand neighbors
            for (CompassDirection direction : CompassDirection.values()) {
                LngLat nextPosition = distanceService.calcNextPosition(
                        new NextPositionRequest(currentPosition, direction.getAngle()));

                // Skip positions outside the central area if drone entered central area
                if (insideCentralArea && !distanceService.isInRegionChecker(
                        new IsInRegionRequest(nextPosition, centralArea))) {
                    continue;
                }

                // Skip no-fly zones
                if (isInNoFlyZone(nextPosition, noFlyZones)) {
                    continue;
                }

                double g = currentNode.g() + distanceService.calcEuclidDist(
                        new LngLatPairRequest(currentPosition, nextPosition));
                double h = calcHeuristic(nextPosition, appletonTowerLocation);

                // If the node is already visited and the new path is not better
                if (allNodes.containsKey(nextPosition) && g >= allNodes.get(nextPosition).g()) {
                    continue;
                }

                Node nextNode = new Node(nextPosition, g, h, currentNode);
                frontier.add(nextNode);
                allNodes.put(nextPosition, nextNode);
            }
        }

        // Reconstruct the path from the goal node
        if (goalNode != null) {
            while (goalNode != null) {
                flyPath.add(0, goalNode.position());
                goalNode = goalNode.parent();
            }
        }

        return flyPath;
    }

    /**
     * Heuristic function for A* (straight-line distance to goal)
     * @param current the current position
     * @param goal the goal position
     * @return the heuristic value
     */
    private double calcHeuristic(LngLat current, LngLat goal) {
        return distanceService.calcEuclidDist(new LngLatPairRequest(current, goal));
    }

    /**
     * Check if a position is valid (inside Central Area, outside no-fly zones, not visited)
     * @param position the position to check
     * @param noFlyZones the list of no-fly zones
     * @return true if the position is valid, false otherwise
     */
    private boolean isInNoFlyZone(LngLat position, List<NamedRegion> noFlyZones) {
        // Check if position is inside no-fly zones
        return noFlyZones.stream().anyMatch(zone ->
                distanceService.isInRegionChecker(new IsInRegionRequest(position, zone)));
    }
}