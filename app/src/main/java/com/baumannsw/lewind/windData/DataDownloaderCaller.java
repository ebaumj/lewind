package com.baumannsw.lewind.windData;

import java.util.ArrayList;

public interface DataDownloaderCaller {
    void onDownloadCompleted(ArrayList<WindDataPoint> data);
    void onDownloadFailed(String errorMessage);
}
