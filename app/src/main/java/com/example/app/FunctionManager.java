package com.example.app;

import android.content.SharedPreferences;
import android.net.Uri;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FunctionManager {


     public String format_currency(int numCost_1){
         String originalString_1 = String.valueOf(numCost_1);

         Long longval_1;
         if (originalString_1.contains(",")) {
             originalString_1 = originalString_1.replaceAll(",", "");
         }
         longval_1 = Long.parseLong(originalString_1);

         DecimalFormat formatter_1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
         formatter_1.applyPattern("#,###,###,###,###,###,###");

         return formatter_1.format(longval_1);
     }

    public static String getUsername(SharedPreferences sharedPreferences){
        return sharedPreferences.getString("username", null);
    }

    public static Uri getImagePath(SharedPreferences sharedPreferences){
        String imagePath = sharedPreferences.getString("image", null);
        return Uri.parse(imagePath);
    }
}
