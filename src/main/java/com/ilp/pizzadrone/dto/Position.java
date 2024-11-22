package com.ilp.pizzadrone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    @JsonProperty("lng")
    private Double longitude;
    @JsonProperty("lat")
    private Double latitude;

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
