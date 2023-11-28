package com.example.app.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

@Entity(tableName = "wallet")
public class wallet {
    @PrimaryKey
    public int id;
    private String available;
    @Nullable
    private String cost;

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getAvailable() {
        return available;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }

    public int getId_wallet() {
        return id;
    }

    public void setId_wallet(int id) {
        this.id = id;
    }
}