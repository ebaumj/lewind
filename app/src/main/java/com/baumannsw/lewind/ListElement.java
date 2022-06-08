package com.baumannsw.lewind;

import com.baumannsw.lewind.stations.WindStation;
import com.baumannsw.lewind.windData.StationData;

import java.util.ArrayList;

public class ListElement {
    private int id;
    private String name;
    private Double wind;
    private Integer rotation;

    public ListElement(WindStation station, StationData data) {
        id = station.getId();
        name = station.getDisplayName();
        if(data != null) {
            rotation = data.getDirection() - 90;
            if(rotation < 0)
                rotation = 0;
            wind = data.getWindAvg();
        }
        else {
            wind = null;
            rotation = null;
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Double getWind() {
        return wind;
    }

    public Integer getRotation() {
        return rotation;
    }
}
