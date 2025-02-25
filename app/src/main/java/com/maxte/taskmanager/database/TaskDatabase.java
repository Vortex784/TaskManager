package com.maxte.taskmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.maxte.taskmanager.ui.rewards.RewardItem;
import com.maxte.taskmanager.ui.tasks.TaskItem;

@Database(entities = {RewardItem.class, TaskItem.class, Points.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;



    public abstract RewardDao rewardDao();
    public abstract TaskDao taskDao();
    public abstract PointsDao pointsDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

