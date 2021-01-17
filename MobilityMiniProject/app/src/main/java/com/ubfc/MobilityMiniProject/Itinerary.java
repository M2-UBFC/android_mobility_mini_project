package com.ubfc.MobilityMiniProject;

public class Itinerary {
    private double distance;
    private int[] path;

    public Itinerary(double distance, int[] path) {
        this.distance = distance;
        this.path = path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int[] getPath() {
        return path;
    }

    public void setPath(int[] path) {
        this.path = path;
    }
}
