package com.example.app;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.util.StateSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.Adapter.*;

import com.example.app.DAO.*;
import com.example.app.Entities.*;

import com.example.app.databinding.FragmentTenthBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class TenthFragment extends Fragment {

    private FragmentTenthBinding binding;

    bill_detail_Adapter adapter;
    int all_cost = 0;

    FunctionManager func = new FunctionManager();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentTenthBinding.inflate(inflater, container, false);

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        bill_detail_DAO bill_DetailDao = database.bill_detailDao();
        bill_DAO bill_Dao = database.billDao();
        wallet_DAO wallet_Dao = database.walletDao();
        String date;

        date = getArguments().getString("date");

        TextView textViewDate = binding.date;
        textViewDate.setText(date);
        String date_temp = textViewDate.getText().toString();

        Glide.with(this)
                .load(R.drawable.cat11)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(binding.img1));


        // take month
        int index = date.indexOf("/") + 1;
        int stringLength = date.length();

        char[] m = new char[7];

        date.getChars(index, index + (stringLength - index), m, 0);
        String month = String.valueOf(m);
        Bundle bundle_tmp = new Bundle();
        bundle_tmp.putString("month_bill",month);



        List<bill_detail> data_bill_details;

        data_bill_details = bill_DetailDao.getItemByDate_bill_detail(date);
        List<bill_detail> temp = data_bill_details;
        if(data_bill_details.size() > 0) {
            RecyclerView rv_incomes = binding.listViewDetailIncome;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            adapter = new bill_detail_Adapter(getContext(), (ArrayList<bill_detail>) temp);
            rv_incomes.setLayoutManager(layoutManager);
            rv_incomes.setAdapter(adapter);

            //divider
            rv_incomes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        for (int i = 0; i < temp.size(); i++){
            bill_detail inc_tmp = temp.get(i);
            all_cost += Integer.parseInt(inc_tmp.getCost().toString().replace(",",""));
        }

        String formattedString_1 = func.format_currency(all_cost);

        TextView textViewAllCost = binding.allCost;
        textViewAllCost.setText(formattedString_1);





        //month = get.getSerializableExtra("month");

        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<bill_detail> data_bill_details_1;
                data_bill_details_1 = bill_DetailDao.getItemByDate_bill_detail(date);
                List<bill_detail> temp = data_bill_details_1;
                if(data_bill_details_1.size() > 0) {
                    RecyclerView rv_incomes = binding.listViewDetailIncome;
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    adapter = new bill_detail_Adapter(getContext(), (ArrayList<bill_detail>) temp);
                    rv_incomes.setLayoutManager(layoutManager);
                    rv_incomes.setAdapter(adapter);
                }
                all_cost = 0;
                for (int i = 0; i < temp.size(); i++){
                    bill_detail inc_tmp = temp.get(i);
                    all_cost += Integer.parseInt(inc_tmp.getCost().toString().replace(",",""));
                }

                String formattedString_1 = func.format_currency(all_cost);
                TextView textViewAllCost = binding.allCost;
                textViewAllCost.setText(formattedString_1);
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<bill_detail> temp = bill_DetailDao.getItemByDate_bill_detail(date_temp);

                if(temp.size() != data_bill_details.size()){
                    for(int i = 0; i < data_bill_details.size(); i++){
                        bill_detail billDetail = data_bill_details.get(i);
                        if(bill_DetailDao.getItemById_bill_detail(billDetail.getID()) == null){
                            bill_DetailDao.insert_bill_detail(billDetail);
                        }
                    }
                }
                NavHostFragment.findNavController(TenthFragment.this)
                        .navigate(R.id.action_TenthFragment_to_SeventhFragment,bundle_tmp);



            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                List<bill_detail> temp = bill_DetailDao.getItemByDate_bill_detail(date_temp);

                if(temp.size() == 0){
                    bill bill = bill_Dao.getItemByDate_bill(date_temp);
                    wallet w = wallet_Dao.getItemById_wallet(0);

                    String amount = "0";
                    if(w.getCost() != null){
                        amount = w.getCost().replace(",","");
                    }
                    String amount_available;
                    amount_available = w.getAvailable().replace(",","");
                    int available = Integer.parseInt(amount_available) + Integer.parseInt(bill.getCost().replace(",",""));


                    int cost = Integer.parseInt(amount) - Integer.parseInt(bill.getCost().replace(",",""));


                    w.setCost(func.format_currency(cost));
                    w.setAvailable(func.format_currency(available));
                    wallet_Dao.update_wallet(w);

                    bill_Dao.delete_bill(bill);

                    FancyToast.makeText(getContext(),
                            getString(R.string.success_insert),
                            FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                            R.drawable.ic_baseline_check_circle_outline_24,
                            false).show();
                    NavHostFragment.findNavController(TenthFragment.this)
                            .navigate(R.id.action_TenthFragment_to_SeventhFragment,bundle_tmp);

                }else {
                    if(temp.size() == data_bill_details.size()){
                        FancyToast.makeText(getContext(),
                                getString(R.string.success_insert),
                                FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                R.drawable.ic_baseline_check_circle_outline_24,
                                false).show();
                        NavHostFragment.findNavController(TenthFragment.this)
                                .navigate(R.id.action_TenthFragment_to_SeventhFragment,bundle_tmp);
                    }else{
                        onButtonShowPopupWindowClick_Submit(view, date_temp, bill_DetailDao, bill_Dao, wallet_Dao, bundle_tmp);
                    }
                }
            }
        });


        binding.formAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //List<bill_detail> temp = bill_DetailDao.getItemByDate_bill_detail(date_temp);

                onButtonShowPopupWindowClick_Add(view, date_temp, bill_DetailDao, bill_Dao, wallet_Dao, data_bill_details);
            }
        });


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void onButtonShowPopupWindowClick_Submit(View view,String date_temp, bill_detail_DAO bill_detailDao, bill_DAO billDao, wallet_DAO walletDao, Bundle bundle_tmp) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);



        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        dimBehind(popupWindow);
        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);

        textView.setText(getString(R.string.ask_update));

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
                int i = 0;
                String cost = "";
                int allCost = 0;
                List<bill_detail> submit_detail = bill_detailDao.getItemByDate_bill_detail(date_temp);


                while (i < submit_detail.size()){
                    bill_detail bill = new bill_detail();
                    bill = submit_detail.get(i);
                    cost = bill.getCost();
                    String amount;
                    amount = cost.replace(",","");
                    allCost += Integer.parseInt(amount);
                    i++;
                }

                bill bill = billDao.getItemByDate_bill(date_temp);
                int inc_cost_temp = 0;
                inc_cost_temp = Integer.parseInt(bill.getCost().replace(",",""));
                int numCost = allCost;

                bill.setCost(func.format_currency(numCost));

                wallet w = walletDao.getItemById_wallet(0);

                String amount = "0";
                if(w.getCost() != null){
                    amount = w.getCost().replace(",","");
                }
                //amount = w.getCost().replace(",","");

                int cost_1 = Integer.parseInt(amount) + numCost - inc_cost_temp;

                String amount_available = w.getAvailable().toString().replace(",","");


                int available = Integer.parseInt(amount_available) - numCost + inc_cost_temp;

                w.setAvailable(func.format_currency(available));
                w.setCost(func.format_currency(cost_1));
                walletDao.update_wallet(w);
                billDao.update_bill(bill);

                popupWindow.dismiss();


                NavHostFragment.findNavController(TenthFragment.this)
                        .navigate(R.id.action_TenthFragment_to_EighthFragment,bundle_tmp);

                FancyToast.makeText(getContext(),
                                getString(R.string.success_insert),
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                R.drawable.ic_baseline_check_circle_outline_24,
                                false)
                        .show();

            }
        });
    }

    public void onButtonShowPopupWindowClick_Add(View view,String date_temp, bill_detail_DAO bill_detailDao, bill_DAO billDao, wallet_DAO walletDao, List<bill_detail> data_bill_detail) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);



        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        dimBehind(popupWindow);
        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);
        textView.setText(getString(R.string.ask_update));

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
                int i = 0;
                String cost = "";
                int allCost = 0;
                List<bill_detail> submit_detail = bill_detailDao.getItemByDate_bill_detail(date_temp);
                if(submit_detail.size() != data_bill_detail.size()){
                    while (i < submit_detail.size()){
                        bill_detail bill = new bill_detail();
                        bill = submit_detail.get(i);
                        cost = bill.getCost();
                        String amount;
                        amount = cost.replace(",","");
                        allCost += Integer.parseInt(amount);
                        i++;
                    }

                    bill bill = billDao.getItemByDate_bill(date_temp);
                    int inc_cost_temp = 0;
                    inc_cost_temp = Integer.parseInt(bill.getCost().replace(",",""));

                    int numCost = allCost;

                    bill.setCost(func.format_currency(numCost));

                    wallet w = walletDao.getItemById_wallet(0);

                    String amount = "0";
                    if(w.getCost() != null){
                        amount = w.getCost().replace(",","");
                    }
                    int available = Integer.parseInt(amount) + numCost - inc_cost_temp;

                    w.setCost(func.format_currency(available));
                    walletDao.update_wallet(w);
                    billDao.update_bill(bill);
                }
                popupWindow.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString("date_from_detail",date_temp);


                NavHostFragment.findNavController(TenthFragment.this)
                        .navigate(R.id.action_TenthFragment_to_NinethFragment,bundle);// four to fifth


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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}