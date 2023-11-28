package com.example.app.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.DAO.*;
import com.example.app.Entities.*;
import com.example.app.*;

import com.example.app.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.widget.PopupWindow;

public class income_Adapter extends RecyclerView.Adapter<income_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<income> incomes;

    public income_Adapter(Context context, ArrayList<income> incomes){
        this.context = context;
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row_income,parent,false);
        return new ViewHolder(v);
    }

    private void onButtonShowPopupWindowClick_Delete(View view,income incomes,LinearLayout layout, String date) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        income_DAO incomeDao = database.incomeDao();

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

                income_detail_DAO income_detailDao = database.income_detailDao();

                List<income_detail> list_income_details = income_detailDao.getItemByDate_income_detail(date);
                for (int i = 0; i < list_income_details.size(); i++) {
                    income_detail inc_details = list_income_details.get(i);
                    income_detailDao.delete_income_detail(inc_details);
                }



                wallet_DAO walletDao = database.walletDao();
                wallet wallet = walletDao.getItemById_wallet(0);
                String amount;
                amount = wallet.getAvailable().toString().replace(",","");

                income inc = incomeDao.getItemByDate_income(date);
                String amount_inc;
                amount_inc = inc.getCost().toString().replace(",","");

                int all_cost = Integer.parseInt(amount) - Integer.parseInt(amount_inc);
                int numCost = all_cost;

                //-----------------format number 1000 to 1,000---------------------------------------------------
                String originalString_1 = String.valueOf(numCost);

                Long longval_1;
                if (originalString_1.contains(",")) {
                    originalString_1 = originalString_1.replaceAll(",", "");
                }
                longval_1 = Long.parseLong(originalString_1);

                DecimalFormat formatter_1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter_1.applyPattern("#,###,###,###,###,###,###");
                String formattedString_1 = formatter_1.format(longval_1);
                //-----------------format number 1000 to 1,000---------------------------------------------------

                wallet.setAvailable(formattedString_1);
                walletDao.update_wallet(wallet);

                incomeDao.delete_income(incomes);
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
        income income = incomes.get(position);
        holder.text_id.setText(String.valueOf(position));
        holder.text_date.setText("Ngày " + income.getDate());
        holder.text_cost.setText("Tổng thu nhập: " + income.getCost());
        if(holder.btnDel != null){
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                boolean visible;
                @Override
                public void onClick(View view) {
                    //xoa chi tiet thu nhap
                    onButtonShowPopupWindowClick_Delete(view, income, holder.layout, income.getDate());
                }
            });
        }
        if(holder.cardView != null){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                boolean visible;
                @Override
                public void onClick(View view) {
                    //xoa chi tiet thu nhap

                    //onButtonShowPopupWindowClick_Income(view, holder.text_date.getText().toString(), holder.layout);

                    Bundle bundle = new Bundle();
                    String tmp = income.getDate();
                    bundle.putString("date",tmp);
                    Navigation.findNavController(view).navigate(R.id.action_FifthFragment_to_SixthFragment,bundle);


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_date, text_cost, text_id;
        Button buttonYes, buttonNo;
        final ViewGroup transitionsContainer;
        ImageButton btnDel;
        LinearLayout layout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_date = itemView.findViewById(R.id.textViewDate);
            text_cost = itemView.findViewById(R.id.textViewCost);
            text_id = itemView.findViewById(R.id.text_id);
            btnDel = itemView.findViewById(R.id.go);
            buttonNo = itemView.findViewById(R.id.buttonNo);
            buttonYes = itemView.findViewById(R.id.buttonYes);
            transitionsContainer = itemView.findViewById(R.id.transitions_container);
            layout = itemView.findViewById(R.id.layout_date);
            cardView = itemView.findViewById(R.id.cardViewMonth);
        }
    }
}
