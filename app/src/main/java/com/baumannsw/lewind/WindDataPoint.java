package com.baumannsw.lewind;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WindDataPoint {
    private int direction;
    private Date date;
    private float average;
    private float gust;
    private float temperature;

    public WindDataPoint(JSONArray array) throws Exception {
        date = Date.from(Instant.ofEpochMilli(Long.parseLong(array.get(0).toString())));
        average = (float) array.getDouble(1);
        gust = (float) array.getDouble(2);
        direction = (int) array.getInt(3);
        if(direction >= 360)
            direction -= 360;
        temperature = Float.parseFloat(array.getString(4));
    }

    public float getAverage() {
        return average;
    }

    public float getGust() {
        return gust;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.GERMAN);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public int getDirectionInt() {
        return direction;
    }

    public String getDirectionString() {
        String[] text = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = 0;
        if(direction < 385)
            index = (int)(((float)direction + 11.25)/22.5);
        if(index > 15)
            index = 0;
        return text[index];
    }
}
