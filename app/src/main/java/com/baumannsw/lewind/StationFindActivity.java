package com.baumannsw.lewind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baumannsw.lewind.stations.StationsDataAccessObject;
import com.baumannsw.lewind.stations.StationsDatabase;
import com.baumannsw.lewind.stations.WindStation;
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

public class StationFindActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private final String TAG = "FIND_ACTIVITY";

    private ArrayList<WindStation> allStations = new ArrayList<>();
    StationsDataAccessObject stationsDatabase;
    FusedLocationProviderClient locationClient;

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.activity_title_find));

        btnAdd = findViewById(R.id.btnAddStation);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        for (int i = 0; i < ids.length; i++)
            allStations.add(new WindStation(ids[i], names[i], lat[i], lon[i]));

        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }).run();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        /*locationClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        });*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.01, 6.987)));
        //googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        for (WindStation station : allStations)
            if(stationsDatabase.findById(station.getId()) == null)
                googleMap.addMarker(new MarkerOptions().position(new LatLng(station.getLatitude(), station.getLongitude())).title(station.getName())).setTag(station);
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        WindStation station = (WindStation)marker.getTag();
        btnAdd.setText(getResources().getString(R.string.btn_add_station_found, station.getName()));
        btnAdd.setOnClickListener(v -> {
            if(stationsDatabase.findById(station.getId()) == null) {
                stationsDatabase.insert(station);
                marker.remove();
                btnAdd.setText(R.string.btn_add_station);
            }
        });
        return false;
    }
}