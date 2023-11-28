package com.example.app.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.example.app.Entities.wallet;

import java.util.List;

@Dao
public interface wallet_DAO {@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Insert
    public void insert_wallet(wallet... wallets);
    @Update
    public void update_wallet(wallet... wallets);
    @Delete
    public void delete_wallet(wallet wallets);
    @Query("SELECT * FROM wallet")
    public List<wallet> getItems_wallet();
    @Query("SELECT * FROM wallet WHERE id = :id")
    public wallet getItemById_wallet(int id);
}