package com.baumannsw.lewind.windData;

public interface StationDownloaderCaller {
    void onStationDownloadCompleted(StationData data);
    void onStationDownloadFailed(String errorMessage);
}
