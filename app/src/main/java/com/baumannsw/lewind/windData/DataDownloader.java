package com.baumannsw.lewind.windData;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataDownloader extends AsyncTask<String, String, String> {

    private DataDownloaderCaller caller;
    private String urlText;
    private int connectionTimeout;
    ArrayList<WindDataPoint> data;

    public DataDownloader(DataDownloaderCaller caller, long id, int connectionTimeout) {
        this.caller = caller;
        this.urlText = "https://lewind.ch/api/android_access/history/" + id;
        this.connectionTimeout = connectionTimeout;
        data = new ArrayList<>();
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
        JSONArray json;
        try {
            json = new JSONArray(s);
            Exception ex = null;
            for(int i = 0; i < json.length(); i++)
                try {
                    WindDataPoint o = new WindDataPoint(json.getJSONArray(i));
                    data.add(new WindDataPoint(json.getJSONArray(i)));
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
