package com.baumannsw.lewind.windData;

public interface StationDownloaderCaller {
    void onStationDownloadCompleted(StationData data, int id);
    void onStationDownloadFailed(String errorMessage, int id);
}
