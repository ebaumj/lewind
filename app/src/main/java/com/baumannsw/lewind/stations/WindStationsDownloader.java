package com.baumannsw.lewind.stations;

import android.os.AsyncTask;
import android.util.Log;

import com.baumannsw.lewind.windData.WindDataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WindStationsDownloader extends AsyncTask<String, String, String> {

    private WindStationsDownloaderCaller caller;
    private final String urlText = "https://lewind.ch/api/android_access/all_stations";
    private ArrayList<StationMap> data;
    private int connectionTimeout;

    public WindStationsDownloader(WindStationsDownloaderCaller caller, int connectionTimeout) {
        this.caller = caller;
        data = new ArrayList<>();
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @param strings
     * @deprecated
     */
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
        JSONArray json;
        try {
            //json = new JSONObject(s).getJSONArray("stations");
            json = new JSONArray(s);
            Exception ex = null;
            for(int i = 0; i < json.length(); i++)
                try {
                    data.add(new StationMap(json.getJSONObject(i)));
                } catch (Exception e) {
                    ex = e;
                }
            if(data.size() > 0)
                caller.onDownloadCompleted(data);
            else if(ex != null)
                caller.onDownloadFailed("JSON Error" + ex.getMessage());
            else
                caller.onDownloadFailed("No Data Available");
        } catch (JSONException e) {
            caller.onDownloadFailed("JSON Error: " + e.getMessage());
        }
    }
}
