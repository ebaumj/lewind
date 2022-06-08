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
            rotation = data.getDirection() + 270;
            if(rotation > 360)
                rotation -= 360;
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
