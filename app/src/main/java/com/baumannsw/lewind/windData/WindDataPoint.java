package com.baumannsw.lewind.windData;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        if(temp != null) {
            if(temp.getClass() == String.class)
                date = Date.from(ZonedDateTime.parse((String) temp).toInstant());
            else
                date = Date.from(Instant.ofEpochMilli(Long.parseLong(temp.toString())));
        }
        temp = array.get(1);
        if(temp != null) {
            if(temp.getClass() == String.class)
                average = Double.parseDouble((String) temp);
            else if(temp.getClass() == Integer.class)
                average = new Double((int) temp);
            else if(temp.getClass() == Double.class)
                average = (double) temp;
        }
        temp = array.get(2);
        if(temp != null) {
            if(temp.getClass() == String.class)
                gust = Double.parseDouble((String) temp);
            else if(temp.getClass() == Integer.class)
                gust = new Double((int) temp);
            else if(temp.getClass() == Double.class)
                gust = (double) temp;
        }
        temp = array.get(3);
        if(temp != null) {
            direction = (int) temp;
            if (direction >= 360)
                direction -= 360;
        }
        temp = array.get(4);
        if(temp != null) {
            if(temp.getClass() == String.class)
                temperature = Double.parseDouble((String) temp);
            else if(temp.getClass() == Integer.class)
                temperature = new Double((int) temp);
            else if(temp.getClass() == Double.class)
                temperature = (double) temp;
        }
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
        if(direction >= 0)
            return direction;
        else
            return direction + 360;
    }
}
