package com.example.app;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.databinding.FragmentFirstBinding;

import com.example.app.Entities.*;
import com.example.app.DAO.*;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class FirstFragment extends Fragment {
    private static final String TAG = "hehe";
    List<wallet> mWallet;
    private FragmentFirstBinding binding;
    String temp = " 1 ";
    Fragment fragment = this;

    public static SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);




        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Toast.makeText(getContext(), ""+dayOfMonth, Toast.LENGTH_SHORT).show();//

            }
        });
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("Chọn ngày để thống kê");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        binding.usedMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getParentFragmentManager(),"haha");
            }

        });

        //setting button


        binding.cardView5.setting.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SettingFragment);
        });
        
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        FancyToast.makeText(getContext(),
                                "" + materialDatePicker.getHeaderText(),
                                FancyToast.LENGTH_LONG,FancyToast.WARNING,
                                R.drawable.ic_baseline_error_outline_24,
                                false).show();
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog
                    }
                });

        binding.buttonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.buttonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SeventhFragment);
            }
        });

        Glide.with(this)
                .load(R.drawable.cat1)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(binding.img1));

        Glide.with(this)
                .load(R.drawable.cat2)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(binding.img2));

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());
//        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "mydb")
//                .allowMainThreadQueries()
//                .build();

        sharedPreferences = Objects.requireNonNull(fragment.getActivity()).getSharedPreferences("info", MODE_PRIVATE);
        getInfo(database, sharedPreferences);

        binding.changeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // animation
                ObjectAnimator.ofFloat(binding.cardView,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.cardView,"translationX",1200f).setDuration(2000).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_ThirdFragment);
                    }
                }, 1230);
                // animation
            }
        });

        wallet_DAO walletDao = database.walletDao();
        if(walletDao.getItemById_wallet(0) == null){
            wallet wallets = new wallet();
            wallets.setId_wallet(0);
            wallets.setAvailable("000");
            wallets.setCost("000");
            walletDao.insert_wallet(wallets);
            FancyToast.makeText(getContext(),
                    "Hãy nhập số tiền đang có!",
                    FancyToast.LENGTH_LONG,FancyToast.WARNING,
                    R.drawable.ic_baseline_error_outline_24,
                    false).show();


            //NavHostFragment.findNavController(FirstFragment.this)
                    //.navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }else {
            wallet w = walletDao.getItemById_wallet(0);

            String numAvailable = w.getAvailable();
            String numCost;
                if(w.getCost() == null){
                    numCost = "0";
                }else{
                    numCost = w.getCost();
                }


            binding.textMoney.setText(numAvailable);
            binding.textMoney1.setText(numCost);
        }



    }


    private void showData(List<wallet> wallet) {
        mWallet = wallet;
        String available,cost;
        wallet w = mWallet.get(0);
        available = w.getAvailable();
        cost = w.getCost();

        binding.textMoney.setText(available);
        binding.textMoney1.setText(cost);
    }

    private void getInfo(AppDatabase database, SharedPreferences sharedPreferences) {
        info_DAO infoDao = database.infoDao();
        info img = infoDao.getItemById(0);
            if (img != null) {
                Glide.with(requireContext())
                        .load(img.getImagePath())
                        .into((binding.cardView5.img));

                binding.cardView5.text0.setText(img.getName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", img.getName());
                editor.putString("image", img.getImagePath());
                editor.apply(); // Hoặc editor.commit();


//                binding.cardView5.text0.setText(FunctionManager.getUsername(sharedPreferences));
//                binding.cardView5.img.setImageURI(FunctionManager.getImageUri(sharedPreferences));

            }
    }

//    public static void setUsername_Image(SharedPreferences sharedPreferences){
//        String username = sharedPreferences.getString("username", null);
//        String imageUri = sharedPreferences.getString("imageUri", null);
//        Uri uri = Uri.parse(imageUri);
//        binding.cardView5.text0.setText(username);
//        binding.cardView5.img.setImageURI(uri);
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}