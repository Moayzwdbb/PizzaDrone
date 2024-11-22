package com.ilp.pizzadrone.dto;

import java.util.ArrayList;

public class Region {
    private String name;
    private ArrayList<Position> vertices;

    /**
     * @return name of the region
     */
    public String getName() {
        return name;
    }

    /**
     * @param name set name of the region
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return vertices of the region
     */
    public ArrayList<Position> getVertices() {
        return vertices;
    }

    /**
     * @param vertices set vertices of the region
     */
    public void setVertices(ArrayList<Position> vertices) {
        this.vertices = vertices;
    }
}
