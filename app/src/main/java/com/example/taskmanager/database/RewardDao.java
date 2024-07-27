package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.ui.rewards.RewardItem;

import java.util.List;

@Dao
public interface RewardDao {
    @Insert
    void insertReward(RewardItem reward);

    @Update
    void updateReward(RewardItem reward);

    @Delete
    void deleteReward(RewardItem reward);

    @Query("SELECT * FROM rewards")
    List<RewardItem> getAllRewards();

    @Query("SELECT * FROM rewards WHERE id = :id")
    RewardItem getRewardById(int id);
}
