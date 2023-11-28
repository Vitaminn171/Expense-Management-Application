package com.example.app;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseSingleton {

    private static AppDatabase INSTANCE;

    private AppDatabaseSingleton() {
        // private constructor to prevent instantiation
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "mydb")
                    .allowMainThreadQueries() // Note: Avoid using this in production
                    .build();
        }
        return INSTANCE;
    }
}

