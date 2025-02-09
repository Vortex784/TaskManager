package com.example.taskmanager.ui.tasks;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private double price;
    private boolean isRepeatable;
    private String category;


    public TaskItem(String title, String description, double price, boolean isRepeatable, String category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.isRepeatable = isRepeatable;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }
}
