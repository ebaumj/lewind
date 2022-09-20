package com.baumannsw.lewind.windData;

import android.content.Context;

import com.baumannsw.lewind.R;

import java.util.ArrayList;

public abstract class WindDirection {
    private static final String[] text = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
    public static String getWindDirectionString(int direction) {
        int index = 0;
        if(direction < 385)
            index = (int)(((float)direction + 11.25)/22.5);
        if(index > 15)
            index = 0;
        return text[index];
    }
}
