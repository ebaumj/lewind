package com.baumannsw.lewind.windData;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StationData {

    /*
    {
      "id": 34971,
      "name": "St-Blaise - Club Ichtus",
      "slug": "st-blaise-club-ichtus",
      "source": "ichtus",
      "altitude": null,
      "last_update": "2022-05-19T23:21:59+00:00",
      "humidite": "75",
      "pression": "1021",
      "vent_direction": 52,
      "vent_direction_cardinal": "NE",
      "vent_vitesse": 0.8,
      "vent_rafale": 2,
      "temp": "19.1",
      "latitute": 47.01,
      "longitude": 6.987,
      "last_update_display": "20/05/2022 à 01:21",
      "last_update_min": 0,
      "source_lien": "<a href=\"https://ichtus.ch/\" target=\"_blank\">Ichtus.ch</a>",
      "notes": "Station météo montée sur le toit de l'école/club Ichtus. Merci à eux pour le partage des données."
    }
     */

    private Date lastUpdate;
    private int lastUpdateMin;
    private double humidity;
    private int direction;
    private double windAvg;
    private double windGust;
    private double temperature;

    public StationData(JSONObject data) throws Exception {
        Object temp;
        SimpleDateFormat sdn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        temp = data.get("last_update");
        if(temp != null)
            lastUpdate = sdn.parse((String) temp);
        temp = data.get("last_update_min");
        if(temp != null)
            lastUpdateMin = (int) temp;
        temp = data.get("humidite");
        if(temp != null)
            humidity = Double.parseDouble((String) temp);
        temp = data.get("vent_direction");
        if(temp != null)
            direction = (int) temp;
        temp = data.get("vent_vitesse");
        if(temp != null)
            windAvg = (double) temp;
        temp = data.get("vent_rafale");
        if(temp != null)
            windGust = (double) temp;
        temp = data.get("temp");
        if(temp != null)
            temperature = Double.parseDouble((String) temp);
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateString(String format) {
        if(lastUpdate == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.GERMAN);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(lastUpdate);
    }

    public int getLastUpdateMin() {
        return lastUpdateMin;
    }

    public String getLastUpdateMinString() {
        int hours = lastUpdateMin / 60;
        String retVal = "";
        if(hours > 0)
            retVal += hours + "h ";
        retVal += (lastUpdateMin - (hours * 60)) + "min";
        return retVal;
    }

    public double getHumidity() { return humidity; }

    public int getDirection() {
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

    public double getWindAvg() { return windAvg; }

    public double getWindGust() {
        return windGust;
    }

    public double getTemperature() {
        return temperature;
    }
}
