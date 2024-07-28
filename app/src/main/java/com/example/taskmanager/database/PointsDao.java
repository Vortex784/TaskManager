package com.example.taskmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PointsDao {
    @Query("SELECT * FROM points WHERE id = 1 LIMIT 1")
    LiveData<Points> getPointsLiveData();

    @Query("SELECT * FROM points WHERE id = 1 LIMIT 1")
    Points getPoints();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Points points);

    @Update
    void update(Points points);
}
