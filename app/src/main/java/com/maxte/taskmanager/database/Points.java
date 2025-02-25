package com.maxte.taskmanager.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "points")
public class Points {

    @PrimaryKey
    private int id = 1; // Singleton entity, always have one row with id = 1
    private double points;

    public Points() {
        // Default constructor for Room
    }

    public Points(double points) {
        this.id = 1; // Ensure singleton ID
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
