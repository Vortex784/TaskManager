package com.maxte.taskmanager.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maxte.taskmanager.ui.tasks.TaskItem;

public class FirestoreHelper {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TaskDao taskDao;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.taskDao = taskDao;
    }

    // Upload Room tasks to Firestore
    public void syncTasksToFirestore() {
        List<TaskItem> localTasks = taskDao.getAllTasksSync(); // Get tasks from Room (synchronously)
        String userId = auth.getCurrentUser().getUid();

        for (TaskItem task : localTasks) {
            if (task.getFirestoreId() == null) { // Only upload new tasks
                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("title", task.getTitle());
                taskMap.put("description", task.getDescription());
                taskMap.put("price", task.getPrice());
                taskMap.put("isRepeatable", task.isRepeatable());
                taskMap.put("category", task.getCategory());
                taskMap.put("userId", userId);

                db.collection("tasks").add(taskMap)
                        .addOnSuccessListener(documentReference -> {
                            task.setFirestoreId(documentReference.getId()); // Save Firestore ID
                            taskDao.update(task); // Update local DB with Firestore ID
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Failed to sync task", e));
            }
        }
    }

    // Download tasks from Firestore and store them in Room
    public void syncTasksFromFirestore() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("tasks")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TaskItem> firestoreTasks = queryDocumentSnapshots.toObjects(TaskItem.class);

                    for (TaskItem task : firestoreTasks) {
                        TaskItem existingTask = taskDao.getTaskByFirestoreId(task.getFirestoreId());

                        if (existingTask == null) { // Only insert if it doesn't exist locally
                            taskDao.insert(task);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to sync from Firestore", e));
    }


    // Save Task to Firestore
    public void addTask(TaskItem task, FirestoreCallback callback) {
        String userId = auth.getCurrentUser().getUid();

        // Firestore does not support auto-generated integer IDs, so we don't include "id"
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("title", task.getTitle());
        taskMap.put("description", task.getDescription());
        taskMap.put("price", task.getPrice());
        taskMap.put("isRepeatable", task.isRepeatable());
        taskMap.put("category", task.getCategory());
        taskMap.put("userId", userId);

        db.collection("tasks").add(taskMap)
                .addOnSuccessListener(documentReference -> callback.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Fetch Tasks from Firestore
    public void getTasks(FirestoreDataCallback callback) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("tasks")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    callback.onDataReceived(queryDocumentSnapshots.toObjects(TaskItem.class));
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Delete Task from Firestore
    public void deleteTask(String firestoreTaskId, FirestoreCallback callback) {
        db.collection("tasks").document(firestoreTaskId).delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Callback Interfaces
    public interface FirestoreCallback {
        void onSuccess(String firestoreTaskId);
        void onFailure(String errorMessage);
    }

    public interface FirestoreDataCallback {
        void onDataReceived(java.util.List<TaskItem> taskItems);
        void onFailure(String errorMessage);
    }
}