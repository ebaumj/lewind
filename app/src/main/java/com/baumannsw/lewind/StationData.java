package com.baumannsw.lewind;

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
    private int humidity;
    private int direction;
    private float windAvg;
    private float windGust;
    private float temperature;

    public StationData(JSONObject data) throws Exception {
        SimpleDateFormat sdn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        lastUpdate = sdn.parse(data.getString("last_update"));
        lastUpdateMin = data.getInt("last_update_min");
        humidity = Integer.parseInt(data.getString("humidite"));
        direction = data.getInt("vent_direction");
        windAvg = data.getInt("vent_vitesse");
        windGust = data.getInt("vent_rafale");
        temperature = Float.parseFloat(data.getString("temp"));
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateString(String format) {
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

    public int getHumidity() {
        return humidity;
    }

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

    public float getWindAvg() {
        return windAvg;
    }

    public float getWindGust() {
        return windGust;
    }

    public float getTemperature() {
        return temperature;
    }
}
