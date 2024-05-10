package com.example.app.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.example.app.Entities.info;

import java.util.List;

@Dao
public interface info_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
@Insert
public void insert(info... infos);
    @Update
    public void update(info... infos);
    @Delete
    public void delete(info images);
    @Query("SELECT * FROM info")
    public List<info> getItems();
    @Query("SELECT * FROM info WHERE id = :id")
    public info getItemById(int id);
}