package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ListView;

import com.baumannsw.lewind.stations.StationsDataAccessObject;
import com.baumannsw.lewind.stations.StationsDatabase;
import com.baumannsw.lewind.stations.WindStation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private final String TAG = "MAIN_ACTIVITY";

    StationsDataAccessObject stationsDatabase;

    private ListView listView;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.activity_title_edit));

        // UI Elements
        listView = findViewById(R.id.listEdit);
        btnAdd = findViewById(R.id.btnAdd);

        // Interactions

        // Database
        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            EditListAdapter adapter = new EditListAdapter(getApplicationContext(), (ArrayList<WindStation>) stationsDatabase.getAll());
            runOnUiThread(() -> listView.setAdapter(adapter));
        }).run();
    }
}