package com.example.app.DAO;

import com.example.app.Entities.Converters;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.app.Entities.income;

import java.util.List;

@Dao
public interface income_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Insert
    public void insert_income(income... incomes);

    @Update
    public void update_income(income... incomes);

    @Delete
    public void delete_income(income incomes);

    @Query("SELECT * FROM income")
    public List<income> getItems_income();

    @Query("SELECT * FROM income WHERE id = :id")
    public income getItemById_income(int id);

    @Query("SELECT * FROM income WHERE date LIKE :date")
    public List<income> getItemByMonth_income(String date);

    @Query("SELECT * FROM income WHERE date = :date")
    public income getItemByDate_income(String date);

}