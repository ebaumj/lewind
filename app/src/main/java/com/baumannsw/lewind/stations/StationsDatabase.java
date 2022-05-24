package com.baumannsw.lewind.stations;

import androidx.room.RoomDatabase;
import androidx.room.Database;

@Database(entities = {WindStation.class}, version = 1, exportSchema = true)
public abstract class StationsDatabase extends RoomDatabase {
    public abstract StationsDataAccessObject stationsDao();
}
