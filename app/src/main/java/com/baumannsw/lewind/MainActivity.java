package com.baumannsw.lewind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baumannsw.lewind.stations.StationsDataAccessObject;
import com.baumannsw.lewind.stations.StationsDatabase;
import com.baumannsw.lewind.stations.WindStation;
import com.baumannsw.lewind.windData.DataDownloader;
import com.baumannsw.lewind.windData.DataDownloaderCaller;
import com.baumannsw.lewind.windData.StationData;
import com.baumannsw.lewind.windData.StationDownloader;
import com.baumannsw.lewind.windData.StationDownloaderCaller;
import com.baumannsw.lewind.windData.WindDataPoint;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DataDownloaderCaller, StationDownloaderCaller {

    private Button btnLoad;
    private TextView tvData;
    private final int stBlaise = 34971;
    private final int estavayer = 15265;

    private final int[] supportedStations = {34971, 15265, 2429, 41825, 2478, 31320};
    private final String[] names = {"St. Blaise", "Estavayer", "Yvonand", "Cudrefin", "La Brévine", "Lac De Joux"};

    private List<WindStation> stationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GUI Elements
        btnLoad = findViewById(R.id.btnLoad);
        tvData = findViewById(R.id.tvData);

        // Setup Database, working, still move to another Thread
        StationsDataAccessObject stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
        if(stationsDatabase.count() == 0) {
            for (int i = 0; i < supportedStations.length; i++)
                stationsDatabase.insert(new WindStation(supportedStations[i], names[i], 0, 0));
        }
        tvData.setText(stationsDatabase.findById(supportedStations[0]).getDisplayName());
    }

    private void startGraphDownload() {
        new DataDownloader(this, stBlaise).execute();
    }
    private void startDownload() {
        new StationDownloader(this, estavayer).execute();
    }

    @Override
    public void onDownloadCompleted(List<WindDataPoint> data) {

    }

    @Override
    public void onDownloadFailed(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStationDownloadCompleted(StationData data) {

    }

    @Override
    public void onStationDownloadFailed(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}