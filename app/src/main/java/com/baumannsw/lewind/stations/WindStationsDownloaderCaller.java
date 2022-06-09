package com.baumannsw.lewind.stations;

import java.util.ArrayList;

public interface WindStationsDownloaderCaller {
    void onDownloadCompleted(ArrayList<StationMap> data);
    void onDownloadFailed(String errorMessage);
}
