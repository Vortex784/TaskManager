package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.ui.tasks.TaskItem;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(TaskItem task);

    @Update
    void updateTask(TaskItem task);

    @Delete
    void deleteTask(TaskItem task);

    @Query("SELECT * FROM tasks")
    List<TaskItem> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskItem getTaskById(int id);
}
