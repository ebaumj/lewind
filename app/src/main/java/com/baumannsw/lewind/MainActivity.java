package com.baumannsw.lewind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements DataDownloaderCaller, StationDownloaderCaller {

    private Button btnLoad;
    private TextView tvData;
    private final String urlText = "https://letskite.ch/datas/station/34971/graph";
    private final int stBlaise = 34971;
    private final String TAG = "LE_WIND";
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoad = findViewById(R.id.btnLoad);
        tvData = findViewById(R.id.tvData);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });

        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvData.setText("");
            }
        });
    }

    private void startGraphDownload() {
        new DataDownloader(this, stBlaise).execute();
    }
    private void startDownload() {
        new StationDownloader(this, stBlaise).execute();
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
        tvData.setText(data.getLastUpdateString("yyyy.MM.dd HH:mm:ss"));
    }

    @Override
    public void onStationDownloadFailed(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}