package com.ubfc.MobilityMiniProject;

public class Location {
    private int id;
    private String name;
    private Float Latitude;
    private Float Longitude;

    public Location(int id, String name, Float latitude, Float longitude) {
        this.id = id;
        this.name = name;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }
}
