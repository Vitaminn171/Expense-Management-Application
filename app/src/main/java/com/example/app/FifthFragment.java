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
import com.example.app.Adapter.income_Adapter;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.wallet;

import com.example.app.DAO.income_DAO;
import com.example.app.Entities.income;

import com.example.app.DAO.income_detail_DAO;
import com.example.app.Entities.income_detail;

import com.example.app.databinding.FragmentFifthBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class FifthFragment extends Fragment {

    private FragmentFifthBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        binding = FragmentFifthBinding.inflate(inflater, container, false);

        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        income_DAO incomeDao = database.incomeDao();
        String month,tempDate;

        month = getArguments().getString("month");
        List<income> data_income;
            data_income = incomeDao.getItemByMonth_income("%" + month);

        if(data_income.size() > 0) {
            RecyclerView rv_incomes = binding.listViewDate;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            income_Adapter adapter = new income_Adapter(getContext(), (ArrayList<income>) data_income);
            rv_incomes.setLayoutManager(layoutManager);
            rv_incomes.setAdapter(adapter);
        }

        //month = get.getSerializableExtra("month");

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FifthFragment.this)
                        .navigate(R.id.action_FifthFragment_to_SecondFragment);
            }
        });

        binding.buttonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FifthFragment.this)
                        .navigate(R.id.action_FifthFragment_to_SeventhFragment);
            }
        });

        binding.formAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FifthFragment.this)
                        .navigate(R.id.action_FifthFragment_to_FouthFragment);
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FifthFragment.this)
                        .navigate(R.id.action_FifthFragment_to_FirstFragment);
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





    public static void remove_Duplicate_Elements(String arr[], int n){
        // Chuyển mảng đã cho thành LinkedHashSet và xoá các phần tử trùng nhau
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(Arrays.asList(arr));

        // Chuyển LinkedHashSet lại trở về mảng
        arr = hashSet.toArray(new String[0]);
    }

}