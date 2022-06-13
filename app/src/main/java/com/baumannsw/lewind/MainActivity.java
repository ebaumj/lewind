package com.baumannsw.lewind;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StationDownloaderCaller {

    private final String TAG = "MAIN_ACTIVITY";

    StationsDataAccessObject stationsDatabase;

    private ListView listStations;
    private FloatingActionButton btnEdit;
    private ArrayList<ListElement> listElements;
    private int elementsCount;
    private AlertDialog waitDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = findViewById(R.id.action_info);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_info) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_about, null));
            builder.setPositiveButton(R.string.dialog_close, ((dialog, which) -> dialog.cancel()));
            AlertDialog dialog = builder.create();
            dialog.show();
            ((TextView)dialog.findViewById(R.id.tvEmail)).setMovementMethod(LinkMovementMethod.getInstance());
            ((TextView)dialog.findViewById(R.id.tvDataProvider)).setMovementMethod(LinkMovementMethod.getInstance());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        // Check Permissions
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // UI Elements
        listStations = findViewById(R.id.listStations);
        btnEdit = findViewById(R.id.btnEdit);

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_wait, null));
        builder.setCancelable(false);
        waitDialog = builder.create();

        // Interactions
        listStations.setOnItemClickListener((parent, view, position, id) -> startStationActivity((int)id));
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Setup Database and get Data
        waitDialog.show();
        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            updateFromDatabase();
        }).run();
    }

    private void updateFromDatabase() {
        listElements = new ArrayList<>();
        elementsCount = stationsDatabase.count();
        if(elementsCount == 0) {
            StationsListAdapter listAdapter = new StationsListAdapter(getApplicationContext(), listElements);
            runOnUiThread(() -> {
                listStations.setAdapter(listAdapter);
                waitDialog.cancel();
            });
        }
        for (WindStation station : stationsDatabase.getAll())
            new StationDownloader(this, station.getId(), getResources().getInteger(R.integer.timeout_http_connection_ms)).execute();
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
                waitDialog.cancel();
            });
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