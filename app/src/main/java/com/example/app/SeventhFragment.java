package com.example.app;

import static java.util.Arrays.asList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.app.*;
import android.widget.ListView;
import android.widget.TextView;
import com.example.app.Adapter.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.app.Adapter.income_detail_Adapter;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.wallet;

import com.example.app.DAO.income_DAO;
import com.example.app.Entities.income;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

import com.example.app.DAO.bill_detail_DAO;
import com.example.app.Entities.bill_detail;

import com.example.app.databinding.FragmentSeventhBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import android.content.SharedPreferences;

public class SeventhFragment extends Fragment {

    private FragmentSeventhBinding binding;
    RecyclerView simpleListView;
    String[] list;
    List<bill_detail> ins;
    List<String> myList;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentSeventhBinding.inflate(inflater, container, false);

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());



        bill_detail_DAO bill_detailDao = database.bill_detailDao();
        String month,tempDate;
        if(bill_detailDao.getItems_bill_detail() != null) {
            ins = bill_detailDao.getItems_bill_detail();
            list = new String[ins.size()];
            for (int i = 0; i < ins.size(); i++) {
                new bill_detail();
                bill_detail in;
                in = ins.get(i);

                tempDate = in.getFormattedDate(); // "DD/MM/YYYY"
                int index = tempDate.indexOf("/") + 1;
                int stringLength = tempDate.length();

                char[] m = new char[7];

                tempDate.getChars(index, index + (stringLength - index), m, 0);
                month = String.valueOf(m);

                list[i] = month;


            }
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(asList(list));
            list = hashSet.toArray(new String[0]);
            myList = Arrays.asList(list);
        }
        ArrayList<String> data = new ArrayList<String>(Arrays.asList(list));
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SeventhFragment.this)
                        .navigate(R.id.action_SeventhFragment_to_FirstFragment);
            }
        });

        binding.buttonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SeventhFragment.this)
                        .navigate(R.id.action_SeventhFragment_to_SecondFragment);
            }
        });

        binding.formAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SeventhFragment.this)
                        .navigate(R.id.action_SeventhFragment_to_NinethFragment);
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SeventhFragment.this)
                        .navigate(R.id.action_SeventhFragment_to_FirstFragment);
            }
        });


        simpleListView = binding.listViewMonthBill;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        Adapter_month_bill adapter = new Adapter_month_bill(getContext(), (ArrayList<String>) data);
        simpleListView.setLayoutManager(layoutManager);
        simpleListView.setAdapter(adapter);


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = FirstFragment.sharedPreferences;
        binding.cardView5.text0.setText(FunctionManager.getUsername(sharedPreferences));
        Glide.with(requireContext())
                .load(FunctionManager.getImagePath(sharedPreferences))
                .into((binding.cardView5.img));

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}