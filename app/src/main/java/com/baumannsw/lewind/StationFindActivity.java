package com.baumannsw.lewind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baumannsw.lewind.stations.StationMap;
import com.baumannsw.lewind.stations.StationsDataAccessObject;
import com.baumannsw.lewind.stations.StationsDatabase;
import com.baumannsw.lewind.stations.WindStation;
import com.baumannsw.lewind.stations.WindStationsDownloader;
import com.baumannsw.lewind.stations.WindStationsDownloaderCaller;
import com.baumannsw.lewind.windData.StationData;
import com.baumannsw.lewind.windData.StationDownloader;
import com.baumannsw.lewind.windData.StationDownloaderCaller;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StationFindActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, StationDownloaderCaller, WindStationsDownloaderCaller {

    private final String TAG = "FIND_ACTIVITY";
    private final LatLng homeLocation = new LatLng(46.9196377, 7.4081305);
    private final float zoom = 9;

    private ArrayList<StationMap> allStations = new ArrayList<>();
    private Marker marker;
    StationsDataAccessObject stationsDatabase;
    //FusedLocationProviderClient locationClient;

    private Button btnAdd;

    private final long[] ids = {34971, 15265, 2429, 41825, 2478, 31320};
    private final String[] names = {"St-Blaise - Club Ichtus", "Estavayer", "STATION D'YVBEACH", "Cudrefin", "La Brévine", "Lac de Joux - Altitude 1004"};
    private final double[] lat = {47.01, 46.850333, 46.807333, 46.96, 46.983844, 46.621};
    private final double[] lon = {6.987, 6.838833, 6.726813, 7.015, 6.610297, 6.265};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_find);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.activity_title_find);

        btnAdd = findViewById(R.id.btnAddStation);

        //locationClient = LocationServices.getFusedLocationProviderClient(this);

        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            new WindStationsDownloader(this).execute();
        }).run();
    }

    private void startMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        /*locationClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        });*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLocation, zoom));

        for (StationMap station : allStations)
            if(stationsDatabase.findById(station.getId()) == null)
                googleMap.addMarker(new MarkerOptions().position(new LatLng(station.getLatitude(), station.getLongitude()))).setTag(station.getId());
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        long id = (long)marker.getTag();
        this.marker = marker;
        new StationDownloader(this, id).execute();
        return false;
    }

    @Override
    public void onStationDownloadCompleted(StationData data, long id) {
        btnAdd.setText(getResources().getString(R.string.btn_add_station_found, data.getName()));
        btnAdd.setOnClickListener(v -> {
            if(stationsDatabase.findById(data.getId()) == null) {
                stationsDatabase.insert(new WindStation(data.getId(), data.getName(), data.getLatitude(), data.getLongitude()));
                marker.remove();
                btnAdd.setText(R.string.btn_add_station);
            }
        });
    }

    @Override
    public void onStationDownloadFailed(String errorMessage, long id) {
        Toast.makeText(getApplicationContext(), R.string.station_info_fail, Toast.LENGTH_SHORT);
        btnAdd.setText(R.string.btn_add_station);
    }

    @Override
    public void onDownloadCompleted(ArrayList<StationMap> data) {
        allStations = data;
        startMap();
    }

    @Override
    public void onDownloadFailed(String errorMessage) {
        Toast.makeText(getApplicationContext(), R.string.all_stations_fail, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}