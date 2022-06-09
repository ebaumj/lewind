package com.baumannsw.lewind.stations;

import org.json.JSONObject;

public class StationMap {
    private long id;
    private double latitude;
    private double longitude;

    public StationMap(JSONObject json) throws Exception {
        this.id = json.getLong("id");
        this.latitude = json.getDouble("latitude");
        this.longitude = json.getDouble("longitude");
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
