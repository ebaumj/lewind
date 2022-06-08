package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StationDownloaderCaller {

    private final int stBlaise = 34971;

    private final int[] supportedStations = {34971, 15265, 2429, 41825, 2478, 31320};
    private final String[] names = {"St. Blaise", "Estavayer", "Yvonand", "Cudrefin", "La Brévine", "Lac De Joux"};

    StationsDataAccessObject stationsDatabase;

    private ListView listStations;
    private FloatingActionButton btnEdit;
    private ArrayList<ListElement> listElements;
    private int elementsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.activity_title_main));

        // Setup Database, working, still move to another Thread
        stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
        if(stationsDatabase.count() == 0) {
            for (int i = 0; i < supportedStations.length; i++)
                stationsDatabase.insert(new WindStation(supportedStations[i], names[i], 0, 0));
        }

        listStations = findViewById(R.id.listStations);
        btnEdit = findViewById(R.id.btnEdit);

        listElements = new ArrayList<>();
        elementsCount = stationsDatabase.count();

        for (WindStation station : stationsDatabase.getAll()) {
            new StationDownloader(this, station.getId()).execute();
        }
    }

    private void startStationActivity(int id) {
        Intent intent = new Intent(getApplicationContext(), StationActivity.class);
        intent.putExtra(StationActivity.EXTRA_STATION_ID, id);
        intent.putExtra(StationActivity.EXTRA_STATION_NAME, stationsDatabase.findById(id).getDisplayName());
        startActivity(intent);
    }

    private void checkUpdate() {
        if(listElements.size() >= elementsCount) {
            StationsListAdapter listAdapter = new StationsListAdapter(getApplicationContext(), listElements);
            runOnUiThread(() -> {
                listStations.setAdapter(listAdapter);
                listStations.setOnItemClickListener((parent, view, position, id) -> startStationActivity((int)id));
            });
        }
    }

    @Override
    public void onStationDownloadCompleted(StationData data, int id) {
        listElements.add(new ListElement(stationsDatabase.findById(id), data));
        checkUpdate();
    }

    @Override
    public void onStationDownloadFailed(String errorMessage, int id) {
        listElements.add(new ListElement(stationsDatabase.findById(id), null));
        checkUpdate();
    }
}