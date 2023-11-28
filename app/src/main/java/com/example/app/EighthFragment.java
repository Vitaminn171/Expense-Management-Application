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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.Adapter.*;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.bill;
import com.example.app.DAO.bill_DAO;
import com.example.app.Entities.bill_detail;

import com.example.app.DAO.income_DAO;
import com.example.app.Entities.income;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

import com.example.app.databinding.FragmentEighthBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class EighthFragment extends Fragment {

    private FragmentEighthBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentEighthBinding.inflate(inflater, container, false);

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        bill_DAO billDao = database.billDao();
        String month,tempDate;

        month = getArguments().getString("month_bill");
        List<bill> data_bill;
        data_bill = billDao.getItemByMonth_bill("%" + month);

        if(data_bill.size() > 0) {
            RecyclerView rv_bills = binding.listViewDateBill;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            bill_Adapter adapter = new bill_Adapter(getContext(), (ArrayList<bill>) data_bill);
            rv_bills.setLayoutManager(layoutManager);
            rv_bills.setAdapter(adapter);
        }


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EighthFragment.this)
                        .navigate(R.id.action_EighthFragment_to_SeventhFragment);
            }
        });

        binding.buttonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EighthFragment.this)
                        .navigate(R.id.action_EighthFragment_to_SecondFragment);
            }
        });

        binding.formAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EighthFragment.this).navigate(R.id.action_EighthFragment_to_NinethFragment);
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EighthFragment.this)
                        .navigate(R.id.action_EighthFragment_to_FirstFragment);
            }
        });


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}