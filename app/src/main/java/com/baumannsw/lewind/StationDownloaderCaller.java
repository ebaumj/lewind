package com.baumannsw.lewind;

import java.util.List;

public interface StationDownloaderCaller {
    public void onStationDownloadCompleted(StationData data);
    public void onStationDownloadFailed(String errorMessage);
}
