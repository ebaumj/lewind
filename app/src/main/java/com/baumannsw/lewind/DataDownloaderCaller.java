package com.baumannsw.lewind;

import java.util.List;

public interface DataDownloaderCaller {
    public void onDownloadCompleted(List<WindDataPoint> data);
    public void onDownloadFailed(String errorMessage);
}
