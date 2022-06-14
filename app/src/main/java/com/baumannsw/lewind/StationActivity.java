package com.baumannsw.lewind;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
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
import com.baumannsw.lewind.windData.WindColor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StationActivity extends AppCompatActivity implements StationDownloaderCaller {

    public static final String EXTRA_STATION_NAME = "com.baumannsw.lewind.STATION_NAME";
    public static final String EXTRA_STATION_ID = "com.baumannsw.lewind.STATION_ID";

    private final String TAG = "STATION_ACTIVITY";

    private int id;
    private String name;
    private TextView tvWindspeed, tvGusts, tvTemp, tvHum, tvPreassure, tvDirection, tvUpdate;
    private ImageView imgRose;
    private FloatingActionButton btnReload, btnHistory;
    private ActionBar actionBar;
    private AlertDialog waitDialog;

    private ActivityResultLauncher<Intent> historyActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        historyActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (result) -> {
                    id = result.getData().getIntExtra(EXTRA_STATION_ID, 0);
                    name = result.getData().getStringExtra(EXTRA_STATION_NAME);
                    loadData();
                });

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        id = getIntent().getIntExtra(EXTRA_STATION_ID, 0);
        name = getIntent().getStringExtra(EXTRA_STATION_NAME);

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

        actionBar.setTitle(name);

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_wait, null));
        builder.setCancelable(false);
        waitDialog = builder.create();

        btnReload.setOnClickListener(v -> loadData());
        btnHistory.setOnClickListener(v -> startHistoryActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void startHistoryActivity() {
        //Toast.makeText(getApplicationContext(), "History not implemented yet!", Toast.LENGTH_SHORT).show();
        // TODO: Station Activity needs id and name when History is closed!
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        intent.putExtra(HistoryActivity.EXTRA_STATION_ID, id);
        intent.putExtra(HistoryActivity.EXTRA_STATION_NAME, name);
        historyActivityLauncher.launch(intent);
    }

    private void loadData() {
        if(id > 0) {
            waitDialog.show();
            new StationDownloader(this, id, getResources().getInteger(R.integer.timeout_http_connection_ms)).execute();
        }
        else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_id), Toast.LENGTH_SHORT).show();
    }

    private void setWindSpeedColors(Double wind, Double gusts) {
        int[] colors;
        LinearLayout windLayout = findViewById(R.id.backWind);
        LinearLayout gustLayout = findViewById(R.id.backGust);
        GradientDrawable windGradient = (GradientDrawable) getResources().getDrawable((R.drawable.windspeed_container), getTheme());
        GradientDrawable gustGradient = (GradientDrawable) getResources().getDrawable((R.drawable.gust_container), getTheme());
        if(wind != null) {
            colors = windGradient.getColors();
            colors[0] = WindColor.getWindColor(getApplicationContext(), wind);//colorList.get(index);
            windGradient.setColors(colors);
            windLayout.setBackground(windGradient);
            windLayout.setElevation(10);
        }
        if(gusts != null) {
            colors = gustGradient.getColors();
            colors[0] = WindColor.getWindColor(getApplicationContext(), gusts);
            gustGradient.setColors(colors);
            gustLayout.setBackground(gustGradient);
            gustLayout.setElevation(10);
        }
    }

    @Override
    public void onStationDownloadCompleted(StationData data, long id) {
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
            waitDialog.cancel();
        });
    }

    @Override
    public void onStationDownloadFailed(String errorMessage, long id) {
        //Log.e(TAG, errorMessage);
        runOnUiThread(() -> {
            waitDialog.cancel();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.station_download_failed), Toast.LENGTH_SHORT).show();
        });
    }
}