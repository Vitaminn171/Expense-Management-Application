package com.example.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.app.DAO.bill_DAO;
import com.example.app.Entities.bill;

import com.example.app.DAO.bill_detail_DAO;
import com.example.app.Entities.bill_detail;

import com.example.app.DAO.image_DAO;
import com.example.app.Entities.image;

import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.wallet;

import com.example.app.DAO.income_DAO;
import com.example.app.Entities.income;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

import com.example.app.Entities.Converters;


@Database(entities = {bill.class,wallet.class,income.class,income_detail.class, bill_detail.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract bill_DAO billDao();
    public abstract wallet_DAO walletDao();
    public abstract income_DAO incomeDao();
    public abstract income_detail_DAO income_detailDao();
    public abstract bill_detail_DAO bill_detailDao();

}



