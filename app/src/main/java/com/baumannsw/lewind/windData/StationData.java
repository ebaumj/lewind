package com.baumannsw.lewind.windData;

import androidx.room.ColumnInfo;

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

    private long id;
    private Date lastUpdate;
    private Integer lastUpdateMin;
    private Double humidity;
    private Integer direction;
    private Double windAvg;
    private Double windGust;
    private Double temperature;
    private Double preassure;
    private String sourceLink;
    private double latitude;
    private double longitude;
    private String name;

    public StationData(JSONObject data) throws Exception {
        Object temp;
        SimpleDateFormat sdn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        id = data.getInt("id");
        name = data.getString("name");
        latitude = data.getDouble("latitute");
        longitude = data.getDouble("longitude");

        try { lastUpdate = sdn.parse((String) data.get("last_update")); } catch(Exception e) { lastUpdate = null; }

        try { lastUpdateMin = (Integer) data.get("last_update_min"); } catch(Exception e) { lastUpdateMin = null; }
        if(lastUpdateMin == null)
            try { lastUpdateMin = Integer.parseInt((String) data.get("last_update_min")); } catch(Exception e) { lastUpdateMin = null; }

        try { humidity = (Double) data.get("humidite"); } catch(Exception e) { humidity = null; }
        if(humidity == null)
            try { humidity = new Double((Integer) data.get("humidite")); } catch(Exception e) { humidity = null; }
        if(humidity == null)
            try { humidity = Double.parseDouble((String) data.get("humidite")); } catch(Exception e) { humidity = null; }

        try { direction = (Integer) data.get("vent_direction"); } catch(Exception e) { direction = null; }
        if(direction == null)
            try { direction = Integer.parseInt((String) data.get("vent_direction")); } catch(Exception e) { direction = null; }

        try { windAvg = (Double) data.get("vent_vitesse"); } catch(Exception e) { windAvg = null; }
        if(windAvg == null)
            try { windAvg = new Double((Integer) data.get("vent_vitesse")); } catch(Exception e) { windAvg = null; }
        if(windAvg == null)
            try { windAvg = Double.parseDouble((String) data.get("vent_vitesse")); } catch(Exception e) { windAvg = null; }

        try { windGust = (Double) data.get("vent_rafale"); } catch(Exception e) { windGust = null; }
        if(windGust == null)
            try { windGust = new Double((Integer) data.get("vent_rafale")); } catch(Exception e) { windGust = null; }
        if(windGust == null)
            try { windGust = Double.parseDouble((String) data.get("vent_rafale")); } catch(Exception e) { windGust = null; }

        try { temperature = (Double) data.get("temp"); } catch(Exception e) { temperature = null; }
        if(temperature == null)
            try { temperature = new Double((Integer) data.get("temp")); } catch(Exception e) { temperature = null; }
        if(temperature == null)
            try { temperature = Double.parseDouble((String) data.get("temp")); } catch(Exception e) { temperature = null; }

        try { preassure = (Double) data.get("pression"); } catch(Exception e) { preassure = null; }
        if(preassure == null)
            try { preassure = new Double((Integer) data.get("pression")); } catch(Exception e) { preassure = null; }
        if(preassure == null)
            try { preassure = Double.parseDouble((String) data.get("pression")); } catch(Exception e) { preassure = null; }

        try { sourceLink = (String) data.get("source_lien"); } catch (Exception e) { sourceLink = null; }
    }

    public Date getLastUpdate() { return lastUpdate; }

    public String getLastUpdateString(String format) {
        if(lastUpdate == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(lastUpdate);
    }

    public Integer getLastUpdateMin() { return lastUpdateMin; }

    public String getLastUpdateMinString() {
        int hours = lastUpdateMin / 60;
        String retVal = "";
        if(hours > 0)
            retVal += hours + "h ";
        retVal += (lastUpdateMin - (hours * 60)) + "min";
        return retVal;
    }

    public Double getHumidity() { return humidity; }

    public Integer getDirection() { return direction; }

    public String getDirectionString() {
        String[] text = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = 0;
        if(direction < 385)
            index = (int)(((float)direction + 11.25)/22.5);
        if(index > 15)
            index = 0;
        return text[index];
    }

    public Double getWindAvg() { return windAvg; }

    public Double getWindGust() { return windGust; }

    public Double getTemperature() { return temperature; }

    public Double getPreassure() { return preassure; }

    public String getSourceLink() { return sourceLink; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getName() { return name; }

    public long getId() { return id; }
}
