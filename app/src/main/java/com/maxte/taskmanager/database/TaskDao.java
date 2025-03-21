package com.maxte.taskmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maxte.taskmanager.ui.tasks.TaskItem;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(TaskItem task);

    @Update
    void updateTask(TaskItem task);

    @Delete
    void deleteTask(TaskItem task);

    @Query("SELECT * FROM tasks WHERE category = :category")
    List<TaskItem> getTasksByCategory(String category);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskItem getTaskById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskItem task);

    @Update
    void update(TaskItem task);

    @Query("SELECT * FROM tasks")
    LiveData<List<TaskItem>> getAllTasks();

    @Query("SELECT * FROM tasks")
    List<TaskItem> getAllTasksSync(); // Used for Firestore sync (runs on background thread)

    @Query("SELECT * FROM tasks WHERE firestoreId = :firestoreId LIMIT 1")
    TaskItem getTaskByFirestoreId(String firestoreId);
}
