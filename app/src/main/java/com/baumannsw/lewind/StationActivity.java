package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baumannsw.lewind.windData.StationData;
import com.baumannsw.lewind.windData.StationDownloader;
import com.baumannsw.lewind.windData.StationDownloaderCaller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StationActivity extends AppCompatActivity implements StationDownloaderCaller {

    public static final String EXTRA_STATION_NAME = "com.baumannsw.lewind.STATION_NAME";
    public static final String EXTRA_STATION_ID = "com.baumannsw.lewind.STATION_ID";

    private final String TAG = "STATION_ACTIVITY";

    private int id;
    private String name;
    private TextView tvStationName, tvWindspeed, tvGusts, tvTemp, tvHum, tvPreassure, tvDirection, tvUpdate;
    private ImageView imgRose;
    private FloatingActionButton btnReload, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.activity_title_station_data));

        id = getIntent().getIntExtra(EXTRA_STATION_ID, 0);
        name = getIntent().getStringExtra(EXTRA_STATION_NAME);

        tvStationName = findViewById(R.id.tvStationName);
        tvWindspeed = findViewById(R.id.tvWindspeed);
        tvGusts = findViewById(R.id.tvGusts);
        tvTemp = findViewById(R.id.tvTemp);
        tvHum = findViewById(R.id.tvHum);
        tvPreassure = findViewById(R.id.tvPreassure);
        tvDirection = findViewById(R.id.tvDirection);
        tvUpdate = findViewById(R.id.tvUpdate);
        imgRose = findViewById(R.id.imgRose);
        btnReload = findViewById(R.id.btnReload);
        btnHistory =findViewById(R.id.btnHistory);

        tvStationName.setText(name);

        btnReload.setOnClickListener(v -> loadData());
        btnHistory.setOnClickListener(v -> {Toast.makeText(getApplicationContext(), "History not implemented yet!", Toast.LENGTH_SHORT).show();});

        loadData();
    }

    private void loadData() {
        if(id > 0)
            new StationDownloader(this, id).execute();
        else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_id), Toast.LENGTH_SHORT).show();
    }

    private void setWindSpeedColors(Double wind, Double gusts) {
        int[] colors;
        int index;
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.clear();
        colorList.add(getResources().getColor(R.color.wind_0, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_2, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_4, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_6, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_8, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_10, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_12, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_14, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_16, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_18, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_20, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_22, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_24, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_26, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_28, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_30, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_32, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_34, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_36, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_38, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_40, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_42, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_44, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_46, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_48, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_50, getTheme()));
        colorList.add(getResources().getColor(R.color.wind_52, getTheme()));
        LinearLayout windLayout = findViewById(R.id.backWind);
        LinearLayout gustLayout = findViewById(R.id.backGust);
        GradientDrawable windGradient = (GradientDrawable) getResources().getDrawable((R.drawable.windspeed_container), getTheme());
        GradientDrawable gustGradient = (GradientDrawable) getResources().getDrawable((R.drawable.gust_container), getTheme());
        if(wind != null) {
            index = (int)(wind/2);
            if(index >= colorList.size())
                index = colorList.size() - 1;
            colors = windGradient.getColors();
            colors[0] = colorList.get(index);
            windGradient.setColors(colors);
            windLayout.setBackground(windGradient);
            windLayout.setElevation(10);
        }
        if(gusts != null) {
            index = (int)(gusts/2);
            if(index >= colorList.size())
                index = colorList.size() - 1;
            colors = gustGradient.getColors();
            colors[0] = colorList.get(index);
            gustGradient.setColors(colors);
            gustLayout.setBackground(gustGradient);
            gustLayout.setElevation(10);
        }
    }

    @Override
    public void onStationDownloadCompleted(StationData data, int id) {
        runOnUiThread(() -> {
            setWindSpeedColors(data.getWindAvg(), data.getWindGust());
            if(data.getWindAvg() != null)
                tvWindspeed.setText(data.getWindAvg() + " " + getResources().getString(R.string.wind_unit));
            if(data.getWindGust() != null)
                tvGusts.setText(data.getWindGust() + " " +  getResources().getString(R.string.wind_unit));
            if(data.getTemperature() != null)
                tvTemp.setText(data.getTemperature() + " " +  getResources().getString(R.string.temp_unit));
            if(data.getHumidity() != null)
                tvHum.setText(data.getHumidity() + " " +  getResources().getString(R.string.hum_unit));
            if(data.getPreassure() != null)
                tvPreassure.setText(Math.round(data.getPreassure()) + " " +  getResources().getString(R.string.preassure_unit));
            if(data.getDirection() != null) {
                tvDirection.setText(data.getDirectionString() + "  " + data.getDirection() + " " +  getResources().getString(R.string.direction_unit));
                Integer dir = data.getDirection() + 180;
                if(dir > 360)
                    dir = dir - 360;
                imgRose.setRotation(dir);
                imgRose.setVisibility(View.VISIBLE);
            }
            if(data.getLastUpdateMin() != null) {
                tvUpdate.setText(getResources().getString(R.string.last_updated_minutes, data.getLastUpdateMinString()));
            }
        });
    }

    @Override
    public void onStationDownloadFailed(String errorMessage, int id) {
        Log.e(TAG, errorMessage);
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), getResources().getString(R.string.station_download_failed), Toast.LENGTH_SHORT).show());
    }
}