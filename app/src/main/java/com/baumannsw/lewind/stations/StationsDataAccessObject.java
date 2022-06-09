package com.baumannsw.lewind.stations;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StationsDataAccessObject {
    @Query("SELECT * FROM windstation")
    List<WindStation> getAll();

    @Query("SELECT * FROM windstation WHERE id IN (:userIds)")
    List<WindStation> loadAllByIds(long[] userIds);

    @Query("SELECT * FROM windstation WHERE id = :id LIMIT 1")
    WindStation findById(long id);

    @Query("SELECT * FROM windstation WHERE displayName LIKE :name LIMIT 1")
    WindStation findByName(String name);

    @Insert
    void insertAll(WindStation... stations);

    @Insert
    void insert(WindStation station);

    @Delete
    void delete(WindStation station);

    @Query("SELECT COUNT(id) FROM windstation")
    int count();
}
