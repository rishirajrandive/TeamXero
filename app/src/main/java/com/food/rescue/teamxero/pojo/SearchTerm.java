package com.food.rescue.teamxero.pojo;

/**
 * Created by pratiksanglikar on 9/24/16.
 */

public class SearchTerm {

    private double latitude;
    private double longitude;
    private int radius;

    public SearchTerm(double latitude, double longitude, int radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
