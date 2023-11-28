package com.example.app.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.example.app.Entities.income_detail;

import java.util.List;

@Dao
public interface income_detail_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)

    @Insert
    public void insert_income_detail(income_detail... income_details);

    @Update
    public void update_income_detail(income_detail... income_details);

    @Delete
    public void delete_income_detail(income_detail income_details);

    @Query("SELECT * FROM income_detail")
    public List<income_detail> getItems_income_detail();

    @Query("SELECT * FROM income_detail WHERE id = :id")
    public income_detail getItemById_income_detail(int id);

    @Query("SELECT * FROM income_detail WHERE date = :date")
    public List<income_detail> getItemByDate_income_detail(String date);
}