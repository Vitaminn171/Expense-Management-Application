package com.example.app;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.app.databinding.FragmentNinethBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import com.example.app.DAO.*;
import com.example.app.Entities.*;
import com.example.app.Adapter.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;

import com.example.app.Adapter.*;

public class NinethFragment extends Fragment {
    private FragmentNinethBinding binding;
    DatePickerDialog picker;

    List<bill_detail> result;
    String date_temp = "";
    Boolean flag = false;
    private List<bill_detail> data;

    FunctionManager func = new FunctionManager();

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNinethBinding.inflate(inflater, container, false);
        binding.outlinedTextField2Text.addTextChangedListener(onTextChangedListener());

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());


        bill_detail_DAO bill_detailDao = database.bill_detailDao();
        bill_DAO billDao = database.billDao();
        wallet_DAO walletDao = database.walletDao();

        String tempDate;
        if(getArguments() != null && getArguments().getString("date_from_detail") != null){
            tempDate = getArguments().getString("date_from_detail");
            date_temp = tempDate;
            flag = true;
            binding.outlinedTextField3Text.setText(date_temp);
            data = bill_detailDao.getItemByDate_bill_detail(date_temp);
        }

        // ----------------------------- Back -----------------------------
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //huy nhap tien
                if(date_temp.equals("")){
                    NavHostFragment.findNavController(NinethFragment.this)
                            .navigate(R.id.action_NinethFragment_to_SeventhFragment);
                    FancyToast.makeText(getContext(),
                                    getString(R.string.cancel_bill_detail_insert),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.WARNING,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();
                }
                else{

                    onButtonShowPopupWindowClick_Back(view,bill_detailDao,billDao,walletDao);

                }

            }
        });
        // ----------------------------- Back -----------------------------


        // ----------------------------- Calendar -----------------------------
        binding.buttonCalendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.outlinedTextField3Text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        // ----------------------------- Calendar -----------------------------




        RecyclerView rv_billDetails = binding.listViewBill;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);



        // ----------------------------- Add -----------------------------
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date,title,cost,amount;
                date = String.valueOf(binding.outlinedTextField3Text.getText());
                title = String.valueOf(binding.outlinedTextField1Text.getText());
                cost = String.valueOf(binding.outlinedTextField2Text.getText());
                amount = cost.replace(",","");


                if (date.equals("") || title.equals("") || cost.equals("") || Integer.parseInt(amount) < 1000) {
                    FancyToast.makeText(getContext(),
                                    getString(R.string.blank_field),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();

                }
                else{
                    List<bill_detail> tmp = bill_detailDao.getItemByDate_bill_detail(date);
                    if(tmp.size() != 0 && date_temp.equals("")){
                        data = tmp;
                        onButtonShowPopupWindowClick_Add(view,date,title,cost,bill_detailDao,rv_billDetails,layoutManager);
                    }else{
                        bill_detail bill_details = new bill_detail();
                        String temp = "";
                        date_temp = date;
                        bill_details.setName(title);
                        bill_details.setDate(date);
                        bill_details.setCost(cost);
                        bill_detailDao.insert_bill_detail(bill_details);

                        List<bill_detail> tempDetail = bill_detailDao.getItemByDate_bill_detail(date);
                        result = tempDetail;


                        bill_detail_Adapter adapter = new bill_detail_Adapter(getContext(), (ArrayList<bill_detail>) tempDetail);
                        rv_billDetails.setLayoutManager(layoutManager);
                        rv_billDetails.setAdapter(adapter);

                        //divider
                        rv_billDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                        FancyToast.makeText(getContext(),
                                        getString(R.string.success_add_detail),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        R.drawable.ic_baseline_error_outline_24,
                                        false)
                                .show();
                    }

                    binding.outlinedTextField3Text.setText(date);
                    binding.outlinedTextField2Text.setText("");
                    binding.outlinedTextField1Text.setText("");
                }
            }
        });
        // ----------------------------- Add -----------------------------



        // ----------------------------- Refresh -----------------------------
        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!date_temp.equals("")) {
                    List<bill_detail> temp = bill_detailDao.getItemByDate_bill_detail(date_temp);
                    bill_detail_Adapter adapter = new bill_detail_Adapter(getContext(), (ArrayList<bill_detail>) temp);
                    rv_billDetails.setLayoutManager(layoutManager);
                    rv_billDetails.setAdapter(adapter);
                }
            }
        });
        // ----------------------------- Refresh -----------------------------




        // ----------------------------- Submit -----------------------------
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //huy nhap tien
                if(date_temp.equals("")){
                    FancyToast.makeText(getContext(),
                                    getString(R.string.blank_field),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.WARNING,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();
                }
                else{
                    onButtonShowPopupWindowClick_Submit(view, date_temp, bill_detailDao, billDao, walletDao);
                }
            }
        });
        // ----------------------------- Submit -----------------------------


        return binding.getRoot();

    }


    public void onButtonShowPopupWindowClick_Back(View view, bill_detail_DAO bill_detailDao, bill_DAO billDao,wallet_DAO walletDao) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);



        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

        dimBehind(popupWindow);
        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);
        textView.setText(R.string.ask_cancel_bill_insert);


        ImageView img1 = popupView.findViewById(R.id.img1);
        Glide.with(popupView.getContext())
                .load(R.drawable.cat10)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(img1));

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                List<bill_detail> temp = bill_detailDao.getItemByDate_bill_detail(date_temp);
                if(billDao.getItemByDate_bill(date_temp) != null){
                    //List<income_detail> temp = income_detailDao.getItemByDate_income_detail(date_temp);
                    for(int i = 0; i < data.size(); i++){
                        bill_detail billDetail = null;
                        boolean flag_test = true;
                        billDetail = data.get(i);

                        if (billDetail != null){
                            for(int j = 0; j < temp.size(); j++){
                                bill_detail billDetail_temp = temp.get(j);
                                if(billDetail_temp.getID() == billDetail.getID()){
                                    flag_test = false;
                                    break;
                                }
                            }
                        }
                        if (flag_test){
                            bill_detailDao.insert_bill_detail(billDetail);
                        }
                    }

                    for(int i = 0; i < temp.size(); i++){
                        bill_detail billDetail_1;
                        boolean flag_test_1 = true;

                        billDetail_1 = temp.get(i);

                        if(billDetail_1 != null){
                            for(int k = 0; k < data.size(); k++){
                                bill_detail billDetail_temp = data.get(k);
                                if(billDetail_temp.getID() == billDetail_1.getID()){
                                    flag_test_1 = false;
                                    break;
                                }
                            }
                        }
                        if (flag_test_1){
                            bill_detailDao.delete_bill_detail(billDetail_1);
                        }

                    }
                }else{
                    for (int i = 0; i < temp.size(); i++){
                        bill_detail billDetail = temp.get(i);
                        bill_detailDao.delete_bill_detail(billDetail);
                    }
                }


                NavHostFragment.findNavController(NinethFragment.this)
                        .navigate(R.id.action_NinethFragment_to_SeventhFragment);
                FancyToast.makeText(getContext(),
                                getString(R.string.cancel_bill),
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                R.drawable.ic_baseline_error_outline_24,
                                false)
                        .show();
                popupWindow.dismiss();

            }
        });
    }


    public void onButtonShowPopupWindowClick_Add(View view, String date, String title, String cost, bill_detail_DAO bill_detailDao, RecyclerView rv_billDetails, LinearLayoutManager layoutManager) {
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
        textView.setText(getString(R.string.ask_date_already_data));

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
                bill_detail bill_details = new bill_detail();
                flag = true;
                String temp = "";
                date_temp = date;
                bill_details.setName(title);
                bill_details.setDate(date);
                bill_details.setCost(cost);
                bill_detailDao.insert_bill_detail(bill_details);

                List<bill_detail> tempDetail = bill_detailDao.getItemByDate_bill_detail(date);
                result = tempDetail;


                popupWindow.dismiss();
                FancyToast.makeText(getContext(),
                                getString(R.string.success_add_detail),
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                R.drawable.ic_baseline_error_outline_24,
                                false)
                        .show();

                bill_detail_Adapter adapter = new bill_detail_Adapter(getContext(), (ArrayList<bill_detail>) tempDetail);
                rv_billDetails.setLayoutManager(layoutManager);
                rv_billDetails.setAdapter(adapter);
                //divider
                rv_billDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }
        });
    }


    public void onButtonShowPopupWindowClick_Submit(View view,String date_temp, bill_detail_DAO bill_detailDao, bill_DAO billDao, wallet_DAO walletDao) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);



        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_VERTICAL,0,0);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        dimBehind(popupWindow);
        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);
        String text = getString(R.string.ask_submit_bill);
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


                int inc_cost_temp = 0;
                if(flag){
                    bill bills = billDao.getItemByDate_bill(date_temp);
                    if(bills != null){
                        String temp = bills.getCost().replace(",","");
                        inc_cost_temp = Integer.parseInt(temp);
                    }
                }

                bill bills = new bill();
                bills.setDate(date_temp);

                int numCost = allCost;

                String formattedString = func.format_currency(numCost);

                bills.setCost(formattedString);
                bill tempDetail = billDao.getItemByDate_bill(date_temp);
                if(tempDetail != null){
                    tempDetail.setCost(formattedString);
                    tempDetail.setDate(date_temp);
                    billDao.update_bill(tempDetail);
                }else{
                    billDao.insert_bill(bills);
                }
                if(walletDao.getItemById_wallet(0) == null){
                    FancyToast.makeText(getContext(),
                            getString(R.string.null_money),
                            FancyToast.LENGTH_LONG,FancyToast.WARNING,
                            R.drawable.ic_baseline_error_outline_24,
                            false).show();


                    //NavHostFragment.findNavController(FirstFragment.this)
                    //.navigate(R.id.action_FirstFragment_to_ThirdFragment);
                }else {
                    wallet w = walletDao.getItemById_wallet(0);

                    String amount;
                    amount = w.getCost().replace(",","");
                    String amount_available;
                    amount_available = w.getAvailable().replace(",","");
                    int available = Integer.parseInt(amount_available) - numCost + inc_cost_temp;

                    int cost_1 = Integer.parseInt(amount) + numCost - inc_cost_temp;

                    w.setCost(func.format_currency(cost_1));
                    w.setAvailable(func.format_currency(available));
                    walletDao.update_wallet(w);

                }



                popupWindow.dismiss();


                NavHostFragment.findNavController(NinethFragment.this)
                        .navigate(R.id.action_NinethFragment_to_SeventhFragment);

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.outlinedTextField2Text.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    binding.outlinedTextField2Text.setText(formattedString);
                    binding.outlinedTextField2Text.setSelection(binding.outlinedTextField2Text.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                binding.outlinedTextField2Text.addTextChangedListener(this);
            }
        };
    }

}
