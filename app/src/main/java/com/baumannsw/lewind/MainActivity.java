package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private final String TAG = "MAIN_ACTIVITY";

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
        actionBar.setTitle(R.string.app_name);

        // UI Elements
        listStations = findViewById(R.id.listStations);
        btnEdit = findViewById(R.id.btnEdit);

        // Interactions
        listStations.setOnItemClickListener((parent, view, position, id) -> startStationActivity((int)id));
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            startActivity(intent);
        });

        // Setup Database and get Data
        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            updateFromDatabase();
        }).run();
    }

    private void updateFromDatabase() {
        listElements = new ArrayList<>();
        elementsCount = stationsDatabase.count();
        for (WindStation station : stationsDatabase.getAll())
            new StationDownloader(this, station.getId()).execute();
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
            runOnUiThread(() -> listStations.setAdapter(listAdapter));
        }
    }

    @Override
    public void onStationDownloadCompleted(StationData data, long id) {
        listElements.add(new ListElement(stationsDatabase.findById(id), data));
        checkUpdate();
    }

    @Override
    public void onStationDownloadFailed(String errorMessage, long id) {
        listElements.add(new ListElement(stationsDatabase.findById(id), null));
        checkUpdate();
    }
}