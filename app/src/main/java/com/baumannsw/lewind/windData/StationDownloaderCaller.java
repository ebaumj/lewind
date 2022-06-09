package com.baumannsw.lewind.windData;

public interface StationDownloaderCaller {
    void onStationDownloadCompleted(StationData data, long id);
    void onStationDownloadFailed(String errorMessage, long id);
}
