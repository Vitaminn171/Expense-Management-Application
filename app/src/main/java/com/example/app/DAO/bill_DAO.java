package com.example.app.DAO;

import com.example.app.Entities.Converters;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;
import androidx.room.TypeConverters;
import com.example.app.Entities.bill;

import java.util.List;

@Dao
public interface bill_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Insert
    public void insert_bill(bill... bills);
    @Update
    public void update_bill(bill... bills);
    @Delete
    public void delete_bill(bill bills);
    @Query("SELECT * FROM bill")
    public List<bill> getItems();

    @Query("SELECT * FROM bill WHERE date LIKE :date")
    public List<bill> getItemByMonth_bill(String date);

    @Query("SELECT * FROM bill WHERE date = :date")
    public bill getItemByDate_bill(String date);

}