package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baumannsw.lewind.stations.StationsDataAccessObject;
import com.baumannsw.lewind.stations.StationsDatabase;
import com.baumannsw.lewind.stations.WindStation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements EditListAdapter.EditListListener {

    private final String TAG = "EDIT_ACTIVITY";

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

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StationFindActivity.class);
            startActivity(intent);
        });

        // Database
        new Thread(() -> {
            stationsDatabase = Room.databaseBuilder(getApplicationContext(), StationsDatabase.class, "WindStationsDatabase").allowMainThreadQueries().build().stationsDao();
            EditListAdapter adapter = new EditListAdapter(getApplicationContext(), (ArrayList<WindStation>) stationsDatabase.getAll(), this);
            runOnUiThread(() -> listView.setAdapter(adapter));
        }).run();
    }

    public void deleteElement(long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_delete_text, stationsDatabase.findById(id).getDisplayName()));
        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            stationsDatabase.delete(stationsDatabase.findById(id));
            EditListAdapter adapter = new EditListAdapter(getApplicationContext(), (ArrayList<WindStation>) stationsDatabase.getAll(), this);
            runOnUiThread(() -> listView.setAdapter(adapter));
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> { });
        builder.create().show();
    }

    public void editElement(long id) {AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText txtName = new EditText(this);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        txtName.setLayoutParams(layout);
        txtName.setText(stationsDatabase.findById(id).getDisplayName());

        builder.setTitle(getResources().getString(R.string.dialog_edit_text, stationsDatabase.findById(id).getDisplayName()));
        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            WindStation station = new WindStation(stationsDatabase.findById(id));
            stationsDatabase.delete(stationsDatabase.findById(id));
            station.setDisplayName(txtName.getText().toString());
            stationsDatabase.insert(station);
            EditListAdapter adapter = new EditListAdapter(getApplicationContext(), (ArrayList<WindStation>) stationsDatabase.getAll(), this);
            runOnUiThread(() -> listView.setAdapter(adapter));
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> { });
        builder.setView(txtName);
        builder.create().show();
    }
}