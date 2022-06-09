package com.baumannsw.lewind.stations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WindStation {
    @PrimaryKey
    private long id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String displayName;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private double latitude;
    @ColumnInfo
    private double longitude;

    public WindStation(long id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        displayName = name;
        description = "";
    }

    public WindStation(WindStation station) {
        this.id = station.getId();
        this.name = station.getName();
        this.latitude = station.getLatitude();
        this.longitude = station.getLongitude();
        displayName = station.getDisplayName();
        description = station.getDescription();
    }

    public void setDisplayName(String displayName) {
        if(displayName.equals(""))
            this.displayName = name;
        else
            this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
