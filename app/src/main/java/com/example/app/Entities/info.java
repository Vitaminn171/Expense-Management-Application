package com.example.app.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "info")
public class info {
    @PrimaryKey
    public int id;
    private String name;
    private String imagePath;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}