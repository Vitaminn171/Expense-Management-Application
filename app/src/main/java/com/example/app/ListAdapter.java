package com.example.app;


import com.example.app.Entities.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class ListAdapter extends ArrayAdapter<String> {

    private final int resourceLayout;
    private final Context mContext;

    public ListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context.getApplicationContext();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }
        String c = getItem(position);

        if (c != null) {
            TextView textView_date = v.findViewById(R.id.textViewMonth);

            textView_date.setText(c);

            CardView cardView = v.findViewById(R.id.cardViewMonth);
            //textView.setText(c.getName());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FancyToast.makeText(getContext(),
                                    "Không để trống thông tin!",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();
                    // hien cac phieu trong 1 thang
                }
            });
        }

        return v;

    }

}


