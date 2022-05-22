package com.baumannsw.lewind.windData;

public interface StationDownloaderCaller {
    public void onStationDownloadCompleted(StationData data);
    public void onStationDownloadFailed(String errorMessage);
}
