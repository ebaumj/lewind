package com.baumannsw.lewind.windData;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StationDownloader extends AsyncTask<String, String, String> {

    private StationDownloaderCaller caller;
    private String urlText;
    private StationData data;
    private int id;

    public StationDownloader(StationDownloaderCaller caller, int id) {
        this.caller = caller;
        urlText = "https://letskite.ch/datas/station/" + id;
        this.id = id;
    }

    @Override
    protected String doInBackground(String... strings) {
        String text = null;
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(urlText).openConnection();
            int responseCode = connection.getResponseCode();
            Log.i("Downloader", "Code: " + responseCode);
            if(responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                text = reader.readLine();
            }
            else
                caller.onStationDownloadFailed("HTTP Code: " + responseCode, id);
            connection.disconnect();
        } catch (Exception e) {
            Log.e("Downloader", "Exception: " + e.getMessage());
            caller.onStationDownloadFailed(e.getMessage(), id);
        }
        return text;
    }

    @Override
    protected void onPostExecute(String s) {
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
