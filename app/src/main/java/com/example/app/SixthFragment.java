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
import com.example.app.Adapter.income_detail_Adapter;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.wallet;

import com.example.app.DAO.income_DAO;
import com.example.app.Entities.income;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

import com.example.app.databinding.FragmentSixthBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class SixthFragment extends Fragment {

    private FragmentSixthBinding binding;
    income_detail_Adapter adapter;
    int all_cost = 0;
    FunctionManager func = new FunctionManager();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentSixthBinding.inflate(inflater, container, false);

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        income_detail_DAO income_DetailDao = database.income_detailDao();
        income_DAO income_Dao = database.incomeDao();
        wallet_DAO wallet_Dao = database.walletDao();
        String date;

        date = getArguments().getString("date");

        TextView textViewDate = binding.date;
        textViewDate.setText(date);
        String date_temp = textViewDate.getText().toString();

        Glide.with(this)
                .load(R.drawable.cat9)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(binding.img1));


        // take month
        int index = date.indexOf("/") + 1;
        int stringLength = date.length();

        char[] m = new char[7];

        date.getChars(index, index + (stringLength - index), m, 0);
        String month = String.valueOf(m);
        Bundle bundle_tmp = new Bundle();
        bundle_tmp.putString("month",month);



        List<income_detail> data_income_details;

        data_income_details = income_DetailDao.getItemByDate_income_detail(date);
        List<income_detail> temp = data_income_details;
        if(data_income_details.size() > 0) {
            RecyclerView rv_incomes = binding.listViewDetailIncome;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            adapter = new income_detail_Adapter(getContext(), (ArrayList<income_detail>) temp);
            rv_incomes.setLayoutManager(layoutManager);
            rv_incomes.setAdapter(adapter);

            //divider
            rv_incomes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        for (int i = 0; i < temp.size(); i++){
            income_detail inc_tmp = temp.get(i);
            all_cost += Integer.parseInt(inc_tmp.getCost().toString().replace(",",""));
        }

        TextView textViewAllCost = binding.allCost;
        textViewAllCost.setText(func.format_currency(all_cost));


        binding.buttonRefresh.setOnClickListener(view -> {

            List<income_detail> data_income_details_1;
            data_income_details_1 = income_DetailDao.getItemByDate_income_detail(date);
            List<income_detail> temp1 = data_income_details_1;
            if(data_income_details_1.size() > 0) {
                RecyclerView rv_incomes = binding.listViewDetailIncome;
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                adapter = new income_detail_Adapter(getContext(), (ArrayList<income_detail>) temp1);
                rv_incomes.setLayoutManager(layoutManager);
                rv_incomes.setAdapter(adapter);
            }
            all_cost = 0;
            for (int i = 0; i < temp1.size(); i++){
                income_detail inc_tmp = temp1.get(i);
                all_cost += Integer.parseInt(inc_tmp.getCost().toString().replace(",",""));
            }
            TextView textViewAllCost1 = binding.allCost;
            textViewAllCost1.setText(func.format_currency(all_cost));
        });


        binding.backButton.setOnClickListener(view -> {

            List<income_detail> temp12 = income_DetailDao.getItemByDate_income_detail(date_temp);

            if(temp12.size() != data_income_details.size()){
                for(int i = 0; i < data_income_details.size(); i++){
                    income_detail incomeDetail = data_income_details.get(i);
                    if(income_DetailDao.getItemById_income_detail(incomeDetail.getID()) == null){
                        income_DetailDao.insert_income_detail(incomeDetail);
                    }
                }
            }
            NavHostFragment.findNavController(SixthFragment.this)
                    .navigate(R.id.action_SixthFragment_to_FifthFragment,bundle_tmp);



        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                List<income_detail> temp = income_DetailDao.getItemByDate_income_detail(date_temp);

                if(temp.size() == 0){
                    income inc = income_Dao.getItemByDate_income(date_temp);
                    wallet w = wallet_Dao.getItemById_wallet(0);

                    String amount;
                    amount = w.getAvailable().replace(",","");
                    int available = Integer.parseInt(amount) - Integer.parseInt(inc.getCost().replace(",",""));

                    w.setAvailable(String.valueOf(available));
                    wallet_Dao.update_wallet(w);

                    income_Dao.delete_income(inc);

                    FancyToast.makeText(getContext(),
                            getString(R.string.success_insert),
                            FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                            R.drawable.ic_baseline_check_circle_outline_24,
                            false).show();
                    NavHostFragment.findNavController(SixthFragment.this)
                            .navigate(R.id.action_SixthFragment_to_FifthFragment,bundle_tmp);

                }else {
                    if(temp.size() == data_income_details.size()){
                        FancyToast.makeText(getContext(),
                                getString(R.string.success_insert),
                                FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                                R.drawable.ic_baseline_check_circle_outline_24,
                                false).show();
                        NavHostFragment.findNavController(SixthFragment.this)
                                .navigate(R.id.action_SixthFragment_to_FifthFragment,bundle_tmp);
                    }else{
                        onButtonShowPopupWindowClick_Submit(view, date_temp, income_DetailDao, income_Dao, wallet_Dao, bundle_tmp);
                    }
                }
            }
        });


        binding.formAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<income_detail> temp = income_DetailDao.getItemByDate_income_detail(date_temp);

                onButtonShowPopupWindowClick_Add(view, date_temp, income_DetailDao, income_Dao, wallet_Dao, data_income_details);




            }
        });


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onButtonShowPopupWindowClick_Submit(View view,String date_temp, income_detail_DAO income_detailDao, income_DAO incomeDao, wallet_DAO walletDao, Bundle bundle_tmp) {
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

                income inc = incomeDao.getItemByDate_income(date_temp);
                int inc_cost_temp = 0;
                inc_cost_temp = Integer.parseInt(inc.getCost().replace(",",""));

                int numCost = allCost;

                inc.setCost(func.format_currency(numCost));

                wallet w = walletDao.getItemById_wallet(0);

                String amount;
                amount = w.getAvailable().replace(",","");
                int available = Integer.parseInt(amount) + numCost - inc_cost_temp;

                w.setAvailable(func.format_currency(available));
                walletDao.update_wallet(w);
                incomeDao.update_income(inc);



                popupWindow.dismiss();


                NavHostFragment.findNavController(SixthFragment.this)
                        .navigate(R.id.action_SixthFragment_to_FifthFragment,bundle_tmp);

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

    public void onButtonShowPopupWindowClick_Add(View view,String date_temp, income_detail_DAO income_detailDao, income_DAO incomeDao, wallet_DAO walletDao, List<income_detail> data_income_detail) {
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
                List<income_detail> submit_detail = income_detailDao.getItemByDate_income_detail(date_temp);
                if(submit_detail.size() != data_income_detail.size()){
                    while (i < submit_detail.size()){
                        income_detail inc = new income_detail();
                        inc = submit_detail.get(i);
                        cost = inc.getCost();
                        String amount;
                        amount = cost.replace(",","");
                        allCost += Integer.parseInt(amount);
                        i++;
                    }

                    income inc = incomeDao.getItemByDate_income(date_temp);
                    int inc_cost_temp = 0;
                    inc_cost_temp = Integer.parseInt(inc.getCost().replace(",",""));


                    int numCost = allCost;

                    inc.setCost(func.format_currency(numCost));

                    wallet w = walletDao.getItemById_wallet(0);

                    String amount;
                    amount = w.getAvailable().replace(",","");
                    int available = Integer.parseInt(amount) + numCost - inc_cost_temp;

                    w.setAvailable(String.valueOf(available));
                    walletDao.update_wallet(w);
                    incomeDao.update_income(inc);
                }
                popupWindow.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString("date_from_detail",date_temp);


                NavHostFragment.findNavController(SixthFragment.this)
                        .navigate(R.id.action_SixthFragment_to_FouthFragment,bundle);// four to fifth


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