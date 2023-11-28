package com.example.app.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "income")
public class income {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    //private LocalDate date;
    private String cost;
    private String date;

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}