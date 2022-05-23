package com.baumannsw.lewind.windData;

import java.util.List;

public interface DataDownloaderCaller {
    void onDownloadCompleted(List<WindDataPoint> data);
    void onDownloadFailed(String errorMessage);
}
