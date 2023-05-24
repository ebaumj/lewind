package com.baumannsw.lewind.windData;

import android.os.AsyncTask;
import android.util.Log;

import com.baumannsw.lewind.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StationDownloader extends AsyncTask<String, String, String> {

    private StationDownloaderCaller caller;
    private String urlText;
    private StationData data;
    private long id;
    private int connectionTimeout;

    public StationDownloader(StationDownloaderCaller caller, long id, int connectionTimeout) {
        this.caller = caller;
        urlText = "https://lewind.ch/api/android_access/details/" + id;
        this.id = id;
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    protected String doInBackground(String... strings) {
        String text = null;
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(urlText).openConnection();
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(connectionTimeout);
            int responseCode = connection.getResponseCode();
            //Log.i("Downloader", "Code: " + responseCode);
            if(responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                text = reader.readLine();
            }
            connection.disconnect();
        } catch (Exception e) {
            //Log.e("Downloader", "Exception: " + e.getMessage());
        }
        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s == null)
            s = " ";
        super.onPostExecute(s);
        JSONObject json;
        try {
            json = new JSONObject(s);
            data = new StationData(json);
            caller.onStationDownloadCompleted(data, id);
        } catch (Exception e) {
            caller.onStationDownloadFailed("JSON Error: " + e.getMessage(), id);
        }
    }
}
