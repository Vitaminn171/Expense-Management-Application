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
import com.example.app.DAO.income_DAO;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.income;
import com.example.app.Entities.wallet;
import com.example.app.databinding.FragmentFouthBinding;
import com.example.app.databinding.FragmentThirdBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

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

public class FouthFragment extends Fragment {
    private FragmentFouthBinding binding;
    DatePickerDialog picker;
    List<income_detail> result;
    String date_temp = "";
    Boolean flag = false;
    private List<income_detail> data;

    FunctionManager func = new FunctionManager();


    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFouthBinding.inflate(inflater, container, false);
        binding.outlinedTextField2Text.addTextChangedListener(onTextChangedListener());

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        income_detail_DAO income_detailDao = database.income_detailDao();
        income_DAO incomeDao = database.incomeDao();
        wallet_DAO walletDao = database.walletDao();

        String tempDate;
        if(getArguments() != null && getArguments().getString("date_from_detail") != null){
            tempDate = getArguments().getString("date_from_detail");
            date_temp = tempDate;
            flag = true;
            binding.outlinedTextField3Text.setText(date_temp);
            data = income_detailDao.getItemByDate_income_detail(date_temp);
        }

        // ----------------------------- Back -----------------------------
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //huy nhap tien
                if(date_temp.equals("")){
                    NavHostFragment.findNavController(FouthFragment.this)
                            .navigate(R.id.action_FouthFragment_to_SecondFragment);
                    FancyToast.makeText(getContext(),
                                    getString(R.string.ask_cancel_income),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.WARNING,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();
                }
                else{

                onButtonShowPopupWindowClick_Back(view,income_detailDao,incomeDao,walletDao);

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
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);

                                // Creating a Calendar instance for today
                                Calendar currentDate = Calendar.getInstance();

                                // Check if the selected date is before today
                                if (currentDate.before(selectedDate)) {
                                    // The selected date is before today, show an error message
                                    FancyToast.makeText(getContext(),
                                                    "Sai ngày!",
                                                    FancyToast.LENGTH_LONG,
                                                    FancyToast.ERROR,
                                                    R.drawable.ic_baseline_error_outline_24,
                                                    false)
                                            .show();
                                    //Toast.makeText(getContext(), "Selected date cannot be before today", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Use String.format to ensure two digits for day and month
                                    String formattedDay = String.format("%02d", dayOfMonth);
                                    String formattedMonth = String.format("%02d", monthOfYear + 1);

                                    binding.outlinedTextField3Text.setText(formattedDay + "/" + formattedMonth + "/" + year);
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        // ----------------------------- Calendar -----------------------------

        RecyclerView rv_incomeDetails = binding.listViewIncome;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        // ----------------------------- Add -----------------------------
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date,title,cost,note,amount;
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
                    //income_detail_DAO income_detailDao = database.income_detailDao();
                    List<income_detail> tmp;
                    tmp = income_detailDao.getItemByDate_income_detail(date);
                    if(tmp.size() != 0 && date_temp.equals("")){
                        data = tmp;
                        onButtonShowPopupWindowClick_Add(view,date,title,cost,income_detailDao,rv_incomeDetails,layoutManager);
                    }else{
                        income_detail income_details = new income_detail();
                        String temp = "";
                        date_temp = date;
                        income_details.setName(title);
                        income_details.setFormattedDate(date);
                        income_details.setCost(cost);
                        income_detailDao.insert_income_detail(income_details);

                        if(date.length() < 9){
                            date_temp = "0" + date;
                        }
                        List<income_detail> tempDetail = income_detailDao.getItemByDate_income_detail(date_temp);
                        result = tempDetail;


                        income_detail_Adapter adapter = new income_detail_Adapter(getContext(), (ArrayList<income_detail>) tempDetail);
                        rv_incomeDetails.setLayoutManager(layoutManager);
                        rv_incomeDetails.setAdapter(adapter);

                        //divider
                        rv_incomeDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
                    List<income_detail> temp = income_detailDao.getItemByDate_income_detail(date_temp);
                    income_detail_Adapter adapter = new income_detail_Adapter(getContext(), (ArrayList<income_detail>) temp);
                    rv_incomeDetails.setLayoutManager(layoutManager);
                    rv_incomeDetails.setAdapter(adapter);
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
                    onButtonShowPopupWindowClick_Submit(view, date_temp, income_detailDao, incomeDao, walletDao);
                }
            }
        });
        // ----------------------------- Submit -----------------------------


        return binding.getRoot();

    }


    public void onButtonShowPopupWindowClick(View view, income_detail_DAO income_detailDao, income_DAO incomeDao,wallet_DAO walletDao) {
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

        textView.setText("");


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

                if(incomeDao.getItemByDate_income(date_temp) == null) {
                    List<income_detail> list_income_details = income_detailDao.getItemByDate_income_detail(date_temp);
                    for (int i = 0; i < list_income_details.size(); i++) {
                        income_detail inc_details = new income_detail();
                        inc_details = list_income_details.get(i);
                        income_detailDao.delete_income_detail(inc_details);
                    }
                }else{
                    int i = 0;
                    String cost = "";
                    int allCost = 0;
                    List<income_detail> submit_detail = income_detailDao.getItemByDate_income_detail(date_temp);


                    while (i < submit_detail.size()){
                        income_detail inc = new income_detail();
                        inc = submit_detail.get(i);
                        cost = inc.getCost();
                        String amount;
                        amount = cost.replace(",","");
                        allCost += Integer.parseInt(amount);
                        i++;
                    }


                    int inc_cost_temp = 0;
                    if(flag){
                        income incomes = incomeDao.getItemByDate_income(date_temp);
                        String temp = incomes.getCost().replace(",","");
                        inc_cost_temp = Integer.parseInt(temp);
                    }

                    income incomes = new income();
                    incomes.setFormattedDate(date_temp);

                    int numCost = allCost;

                    String originalString = String.valueOf(numCost);

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    incomes.setCost(formattedString);
                    income tempDetail = incomeDao.getItemByDate_income(date_temp);
                    if(tempDetail != null){
                        incomeDao.getItemByDate_income(date_temp);
                        incomeDao.update_income(incomes);
                    }else{
                        incomeDao.insert_income(incomes);
                    }
                    if(walletDao.getItemById_wallet(0) == null){
                        FancyToast.makeText(getContext(),
                                "Chưa nhập số tiền đang có!",
                                FancyToast.LENGTH_LONG,FancyToast.WARNING,
                                R.drawable.ic_baseline_error_outline_24,
                                false).show();


                        //NavHostFragment.findNavController(FirstFragment.this)
                        //.navigate(R.id.action_FirstFragment_to_ThirdFragment);
                    }else {
                        wallet w = walletDao.getItemById_wallet(0);

                        String amount;
                        amount = w.getAvailable().replace(",","");
                        int available = Integer.parseInt(amount) + numCost - inc_cost_temp;



                        //DecimalFormat formatter_1 = new DecimalFormat("#,###,###");
                        //String afterFormat_Available = formatter_1.format(available);
                        w.setAvailable(String.valueOf(available));
                        walletDao.update_wallet(w);

                    }
                }

                NavHostFragment.findNavController(FouthFragment.this)
                        .navigate(R.id.action_FouthFragment_to_SecondFragment);
                FancyToast.makeText(getContext(),
                                "Hủy nhập phiếu chi tiết thu nhập!",
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                R.drawable.ic_baseline_error_outline_24,
                                false)
                        .show();
                popupWindow.dismiss();

            }
        });
    }

    public void onButtonShowPopupWindowClick_Back(View view, income_detail_DAO income_detailDao, income_DAO incomeDao,wallet_DAO walletDao) {
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
        String text = getString(R.string.ask_cancel_income_1);
        textView.setText(text);


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
                List<income_detail> temp = income_detailDao.getItemByDate_income_detail(date_temp);
                if(incomeDao.getItemByDate_income(date_temp) != null){
                    //List<income_detail> temp = income_detailDao.getItemByDate_income_detail(date_temp);
                    for(int i = 0; i < data.size(); i++){
                        income_detail incomeDetail = null;
                        boolean flag_test = true;
                        incomeDetail = data.get(i);

                        if (incomeDetail != null){
                            for(int j = 0; j < temp.size(); j++){
                                income_detail incomeDetail_temp = temp.get(j);
                                if(incomeDetail_temp.getID() == incomeDetail.getID()){
                                    flag_test = false;
                                    break;
                                }
                            }
                        }
                        if (flag_test){
                            income_detailDao.insert_income_detail(incomeDetail);
                        }
                    }

                    for(int i = 0; i < temp.size(); i++){
                        income_detail incomeDetail_1;
                        boolean flag_test_1 = true;

                        incomeDetail_1 = temp.get(i);

                        if(incomeDetail_1 != null){
                            for(int k = 0; k < data.size(); k++){
                                income_detail incomeDetail_temp = data.get(k);
                                if(incomeDetail_temp.getID() == incomeDetail_1.getID()){
                                    flag_test_1 = false;
                                    break;
                                }
                            }
                        }
                        if (flag_test_1){
                            income_detailDao.delete_income_detail(incomeDetail_1);
                        }

                    }
                }else{
                    for (int i = 0; i < temp.size(); i++){
                        income_detail incomeDetail = temp.get(i);
                        income_detailDao.delete_income_detail(incomeDetail);
                    }
                }


                NavHostFragment.findNavController(FouthFragment.this)
                        .navigate(R.id.action_FouthFragment_to_SecondFragment);
                FancyToast.makeText(getContext(),
                                getString(R.string.ask_cancel_income),
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                R.drawable.ic_baseline_error_outline_24,
                                false)
                        .show();
                popupWindow.dismiss();

            }
        });
    }


    public void onButtonShowPopupWindowClick_Add(View view, String date, String title, String cost, income_detail_DAO income_detailDao, RecyclerView rv_incomeDetails, LinearLayoutManager layoutManager) {
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
                income_detail income_details = new income_detail();
                flag = true;
                String temp = "";
                date_temp = date;
                income_details.setName(title);
                income_details.setFormattedDate(date);
                income_details.setCost(cost);
                income_detailDao.insert_income_detail(income_details);


                //List<income_detail> data_income_details;
                //
                //        data_income_details = income_DetailDao.getItemByDate_income_detail(date);
                List<income_detail> tempDetail = income_detailDao.getItemByDate_income_detail(date);
                result = tempDetail;


                popupWindow.dismiss();
                FancyToast.makeText(getContext(),
                                getString(R.string.success_add_detail),
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                R.drawable.ic_baseline_error_outline_24,
                                false)
                        .show();

                income_detail_Adapter adapter = new income_detail_Adapter(getContext(), (ArrayList<income_detail>) tempDetail);
                rv_incomeDetails.setLayoutManager(layoutManager);
                rv_incomeDetails.setAdapter(adapter);
                //divider
                rv_incomeDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }
        });
    }


    public void onButtonShowPopupWindowClick_Submit(View view,String date_temp, income_detail_DAO income_detailDao, income_DAO incomeDao, wallet_DAO walletDao) {
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

        textView.setText(getString(R.string.confirm_add_income));

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
                List<income_detail> submit_detail = income_detailDao.getItemByDate_income_detail(date_temp);


                while (i < submit_detail.size()){
                    income_detail inc = new income_detail();
                    inc = submit_detail.get(i);
                    cost = inc.getCost();
                    String amount;
                    amount = cost.replace(",","");
                    allCost += Integer.parseInt(amount);
                    i++;
                }


                int inc_cost_temp = 0;
                if(flag){
                    income incomes = incomeDao.getItemByDate_income(date_temp);
                    if(incomes != null){
                        String temp = incomes.getCost().replace(",","");
                        inc_cost_temp = Integer.parseInt(temp);
                    }
                }

                income incomes = new income();
                incomes.setFormattedDate(date_temp);

                int numCost = allCost;

                String formattedString = func.format_currency(numCost);

                incomes.setCost(formattedString);
                income tempDetail = incomeDao.getItemByDate_income(date_temp);
                if(tempDetail != null){
                    tempDetail.setCost(formattedString);
                    tempDetail.setFormattedDate(date_temp);
                    incomeDao.update_income(tempDetail);
                }else{
                    incomeDao.insert_income(incomes);
                }
                if(walletDao.getItemById_wallet(0) == null){
                    FancyToast.makeText(getContext(),
                            getString(R.string.null_money),
                            FancyToast.LENGTH_LONG,FancyToast.WARNING,
                            R.drawable.ic_baseline_error_outline_24,
                            false).show();

                }else {
                    wallet w = walletDao.getItemById_wallet(0);

                    String amount;
                    amount = w.getAvailable().replace(",","");
                    int available = Integer.parseInt(amount) + numCost - inc_cost_temp;

                    w.setAvailable(func.format_currency(available));
                    walletDao.update_wallet(w);

                }



                popupWindow.dismiss();


                NavHostFragment.findNavController(FouthFragment.this)
                        .navigate(R.id.action_FouthFragment_to_SecondFragment);// four to fifth

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
