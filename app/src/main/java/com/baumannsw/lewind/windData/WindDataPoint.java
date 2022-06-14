package com.baumannsw.lewind.windData;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WindDataPoint {
    private int direction;
    private Date date;
    private double average;
    private double gust;
    private double temperature;

    public WindDataPoint(JSONArray array) throws Exception {
        Object temp;
        temp = array.get(0);
        if(temp != null)
            date = Date.from(Instant.ofEpochMilli(Long.parseLong(temp.toString())));
        temp = array.get(1);
        if(temp != null)
            average = (double) temp;
        temp = array.get(2);
        if(temp != null)
            gust = (double) temp;
        temp = array.get(3);
        if(temp != null) {
            direction = (int) temp;
            if (direction >= 360)
                direction -= 360;
        }
        temp = array.get(4);
        if(temp != null)
            temperature = Double.parseDouble((String) temp);
    }

    public double getAverage() {
        return average;
    }

    public double getGust() {
        return gust;
    }

    public double getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(String format) {
        if(date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
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
