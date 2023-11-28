package com.example.app.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.example.app.Entities.bill_detail;

import java.util.List;

@Dao
public interface bill_detail_DAO {

    @Insert
    public void insert_bill_detail(bill_detail... bill_details);

    @Update
    public void update_bill_detail(bill_detail... bill_details);

    @Delete
    public void delete_bill_detail(bill_detail bill_details);

    @Query("SELECT * FROM bill_detail")
    public List<bill_detail> getItems_bill_detail();

    @Query("SELECT * FROM bill_detail WHERE id = :id")
    public bill_detail getItemById_bill_detail(int id);

    @Query("SELECT * FROM bill_detail WHERE date = :date")
    public List<bill_detail> getItemByDate_bill_detail(String date);
}