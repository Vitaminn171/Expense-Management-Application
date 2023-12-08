package com.example.app.Entities;
import androidx.room.TypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @TypeConverter
    public static Date fromString(String value) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}

