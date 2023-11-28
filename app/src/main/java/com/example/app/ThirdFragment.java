package com.example.app;



import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.DAO.wallet_DAO;
import com.example.app.Entities.wallet;
import com.example.app.databinding.FragmentThirdBinding;
import com.shashank.sony.fancytoastlib.FancyToast;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;




public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        AppDatabase database = AppDatabaseSingleton.getInstance(requireContext());

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        binding.insertMoney.addTextChangedListener(onTextChangedListener());



        //----------- Hủy nhập tiền
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //huy nhap tien

                // animation
                ObjectAnimator.ofFloat(binding.view,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.view,"translationY",1000f).setDuration(2000).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"translationY",1000f).setDuration(2000).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        binding.view.setVisibility(View.GONE);
                        binding.buttonSubmit.setVisibility(View.GONE);
                        NavHostFragment.findNavController(ThirdFragment.this)
                                .navigate(R.id.action_ThirdFragment_to_FirstFragment);
                        FancyToast.makeText(getContext(),
                                        getString(R.string.Cancel_insert_money),
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.WARNING,
                                        R.drawable.ic_baseline_error_outline_24,
                                        false)
                                .show();
                    }
                }, 1300);
                // animation
            }
        });

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // animation
                ObjectAnimator.ofFloat(binding.view,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.view,"translationY",1000f).setDuration(2000).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"translationY",1000f).setDuration(2000).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        binding.view.setVisibility(View.GONE);
                        binding.buttonSubmit.setVisibility(View.GONE);
                        NavHostFragment.findNavController(ThirdFragment.this)
                                .navigate(R.id.action_ThirdFragment_to_FirstFragment);
                        FancyToast.makeText(getContext(),
                                        getString(R.string.Cancel_insert_money),
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.WARNING,
                                        R.drawable.ic_baseline_error_outline_24,
                                        false)
                                .show();
                    }
                }, 1300);
                // animation
            }
        });


        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // animation
                ObjectAnimator.ofFloat(binding.view,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.view,"translationY",1000f).setDuration(2000).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"alpha",1f,0f).setDuration(1200).start();
                ObjectAnimator.ofFloat(binding.buttonSubmit,"translationY",1000f).setDuration(2000).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        binding.view.setVisibility(View.GONE);
                        binding.buttonSubmit.setVisibility(View.GONE);
                        NavHostFragment.findNavController(ThirdFragment.this)
                                .navigate(R.id.action_ThirdFragment_to_FirstFragment);
                        FancyToast.makeText(getContext(),
                                        getString(R.string.Cancel_insert_money),
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.WARNING,
                                        R.drawable.ic_baseline_error_outline_24,
                                        false)
                                .show();
                    }
                }, 1300);
                // animation
            }
        });


        //----------- Bấm nút xác nhận nhập tiền
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            // xác nhận nhập tiền
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                            String amount,amount_1;
                            amount = binding.insertMoney.getText().toString().replace(",","");
                            amount_1 = binding.insertMoney.getText().toString();
                            if(!amount.equals("") && Integer.parseInt(amount) >= 1000) {

                                wallet_DAO walletDao = database.walletDao();
                                wallet wallets = new wallet();
                                String temp = "";
                                if (walletDao.getItemById_wallet(0) != null) {
                                    //temp = wallets_0.getCost();
                                    wallet wallets_0 = new wallet();
                                    wallets_0 = walletDao.getItemById_wallet(0);
                                    if (wallets_0.getCost() == null) {
                                        temp = "0";
                                    } else {
                                        temp = wallets_0.getCost();
                                    }
                                }


                                wallets.setAvailable(amount_1);
                                wallets.setCost(temp);
                                walletDao.getItemById_wallet(0);
                                walletDao.update_wallet(wallets);

                                ObjectAnimator.ofFloat(binding.buttonSubmit,"alpha",1f,0f).setDuration(800).start();
                                ObjectAnimator.ofFloat(binding.buttonSubmit,"alpha",0f,1f).setDuration(800).start();
                                //ObjectAnimator.ofFloat(binding.buttonSubmit,"translationY",1000f).setDuration(2000).start();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        binding.buttonSubmit.setText("Ok");

                                        handler.postDelayed(new Runnable() {
                                            public void run() {


                                                NavHostFragment.findNavController(ThirdFragment.this)
                                                        .navigate(R.id.action_ThirdFragment_to_FirstFragment);
                                                FancyToast.makeText(getContext(),
                                                                getString(R.string.success_insert),
                                                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                                                R.drawable.ic_baseline_check_circle_outline_24,
                                                                false)
                                                        .show();
                                            }
                                        }, 1020);

                                    }
                                }, 300);
                                // animation


                            }
                            else {
                                // Nhập sai hoặc thiếu
                                FancyToast.makeText(getContext(),
                                                getString(R.string.have_blank_fileds),
                                                FancyToast.LENGTH_LONG,FancyToast.ERROR,
                                                R.drawable.ic_baseline_error_outline_24,
                                                false)
                                        .show();
                            }
                        }

            });
        Glide.with(this)
                .load(R.drawable.cat5)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(binding.img3));
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
                binding.insertMoney.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    binding.insertMoney.setText(formattedString);
                    binding.insertMoney.setSelection(binding.insertMoney.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                binding.insertMoney.addTextChangedListener(this);
            }
        };
    }


}