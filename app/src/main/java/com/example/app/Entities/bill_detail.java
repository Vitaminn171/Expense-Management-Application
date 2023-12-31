package com.example.app.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bill_detail")
public class bill_detail {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    private String name;
    //private LocalDate date;
    private String cost;
    private Date date;

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return Converters.dateToString(date);
    }

    public void setFormattedDate(String dateString) {
        this.date = Converters.fromString(dateString);
    }

}