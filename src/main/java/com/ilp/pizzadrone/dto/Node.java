package com.ilp.pizzadrone.dto;

/**
 * This class is a Node object that is used in the A* algorithm.
 * It contains the position of the node, the g value, and the h value.
 * g is the cost of the path from the start node to the current node.
 * h is the heuristic value of the node.
 */
public class Node implements Comparable<Node> {
    private final LngLat position;
    private final double g;
    private final double h;
    private final double f;
    private final Node parent;

    /**
     * Constructor for the Node object
     * @param position the position of the node
     * @param g the cost of the path from the start node to the current node
     * @param h the heuristic value of the node
     * @param parent the parent node
     * the f value of the node (g + h)
     */
    public Node(LngLat position, double g, double h, Node parent) {
        this.position = position;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
    }

    /**
     * Get the position of the node
     * @return the position of the node
     */
    public LngLat position() {
        return position;
    }

    /**
     * Get the g value of the node
     * @return the g value of the node
     */
    public double g() {
        return g;
    }

    /**
     * Get the h value of the node
     * @return the h value of the node
     */
    public double h() {
        return h;
    }

    /**
     * Get the f value of the node (g + h)
     * @return the f value of the node
     */
    public double f() {
        return f;
    }

    /**
     * Get the parent node
     * @return the parent node
     */
    public Node parent() {
        return parent;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }

}
