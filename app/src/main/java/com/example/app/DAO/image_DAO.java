package com.example.app.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.example.app.Entities.image;

import java.util.List;

@Dao
public interface image_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
@Insert
public void insert(image... images);
    @Update
    public void update(image... images);
    @Delete
    public void delete(image images);
    @Query("SELECT * FROM image")
    public List<image> getItems();
    @Query("SELECT * FROM image WHERE id = :id")
    public image getItemById(int id);
}