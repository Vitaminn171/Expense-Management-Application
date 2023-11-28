package com.example.app.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.DAO.income_DAO;
import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.*;
import com.example.app.*;

import com.example.app.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.PopupWindow;

public class Adapter_month_bill extends RecyclerView.Adapter<Adapter_month_bill.ViewHolder> {

    private Context context;
    private ArrayList<String> month;

    public Adapter_month_bill(Context context, ArrayList<String> month){
        this.context =context;
        this.month = month;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row_month_bill,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String months = month.get(position);
        if(holder.text_month != null){
            holder.text_month.setText("Tháng " + months);
            holder.text_id.setText(String.valueOf(position));
        }
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        RecyclerView rv_billDetails = holder.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        if(holder.cardView != null){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                boolean visible;
                @Override
                public void onClick(View view) {
                    //xoa chi tiet thu nhap

                    String month_1;
                    month_1 = holder.text_month.getText().toString();

                    /*
                    income_DAO incomeDao = database.incomeDao();
                    List<income> tempDetail = incomeDao.getItemByMonth_income(month_1);

                    income_Adapter adapter = new income_Adapter(context, (ArrayList<income>) tempDetail);
                    rv_incomeDetails.setLayoutManager(layoutManager);
                    rv_incomeDetails.setAdapter(adapter);

                     */

                    Bundle bundle = new Bundle();
                    String tmp = month_1.replace("Tháng ", "");
                    bundle.putString("month_bill",tmp);
                    Navigation.findNavController(view).navigate(R.id.action_SeventhFragment_to_EighthFragment,bundle);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return month.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_month,text_id;
        CardView cardView;
        RecyclerView recyclerView;
        ViewParent viewParent;
        View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_month = itemView.findViewById(R.id.textViewMonth);
            text_id = itemView.findViewById(R.id.text_id);
            cardView = itemView.findViewById(R.id.cardViewMonth);
            viewParent = itemView.getParent();
            view = itemView.getRootView();
            recyclerView = view.findViewById(R.id.listViewIncome);
        }
    }
}
