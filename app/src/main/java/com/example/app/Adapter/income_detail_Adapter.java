package com.example.app.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.*;
import com.example.app.*;

import com.example.app.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.PopupWindow;

public class income_detail_Adapter extends RecyclerView.Adapter<income_detail_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<income_detail> income_details;

    public income_detail_Adapter(Context context, ArrayList<income_detail> income_details){
        this.context =context;
        this.income_details = income_details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row_income_temp,parent,false);
        return new ViewHolder(v);
    }

    private void onButtonShowPopupWindowClick(View view,income_detail incomeDetails,LinearLayout layout) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        income_detail_DAO income_detailDao = database.income_detailDao();

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);

        dimBehind(popupWindow);

        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);
        String text = "Xóa chi tiết phiếu này ?";
        textView.setText(text);

        ImageView img1 = popupView.findViewById(R.id.img1);
        Glide.with(popupView.getContext())
                .load(R.drawable.cat10)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(img1));
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                income_detailDao.delete_income_detail(incomeDetails);
                popupWindow.dismiss();

                // animation slide over the screen  (slide into right)
                ObjectAnimator.ofFloat(layout,"translationX",-1200f).setDuration(1500).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        layout.setVisibility(View.GONE);
                    }
                }, 1500);



            }
        });
    }


    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
                container = (View) popupWindow.getContentView().getParent();
        } else {
                container = (View) popupWindow.getContentView().getParent().getParent();

        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        income_detail incomeDetails = income_details.get(position);
        holder.text_id.setText(String.valueOf(position));
        holder.text_title.setText(incomeDetails.getName());
        holder.text_date.setText(incomeDetails.getDate());
        holder.text_cost.setText(incomeDetails.getCost());
        if(holder.btnDel != null){
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                boolean visible;
                @Override
                public void onClick(View view) {
                    //xoa chi tiet thu nhap

                    onButtonShowPopupWindowClick(view, incomeDetails, holder.layout);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return income_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title, text_date, text_cost, text_id;
        Button buttonYes, buttonNo;
        final ViewGroup transitionsContainer;
        ImageButton btnDel;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_title);
            text_date = itemView.findViewById(R.id.text_date);
            text_cost = itemView.findViewById(R.id.text_cost);
            text_id = itemView.findViewById(R.id.text_id);
            btnDel = itemView.findViewById(R.id.delete);
            buttonNo = itemView.findViewById(R.id.buttonNo);
            buttonYes = itemView.findViewById(R.id.buttonYes);
            transitionsContainer = itemView.findViewById(R.id.transitions_container);
            layout = itemView.findViewById(R.id.layout_income_detail);
        }
    }
}
