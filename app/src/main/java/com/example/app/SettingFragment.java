package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.app.Entities.info;
import com.example.app.databinding.FragmentSettingBinding;

import com.google.android.material.imageview.ShapeableImageView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.example.app.DAO.info_DAO;


public class SettingFragment extends Fragment {

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    ShapeableImageView imageView;
    private FragmentSettingBinding binding;

    static Uri selectedImageUri;
    AppDatabase database;
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        database = AppDatabaseSingleton.getInstance(requireContext());
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FancyToast.makeText(getContext(),
                                getString(R.string.cancel_info),
                                FancyToast.LENGTH_LONG,
                                FancyToast.WARNING,
                                R.drawable.ic_baseline_check_circle_outline_24,
                                false)
                        .show();

                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_SettingFragment_to_FirstFragment);
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = String.valueOf(binding.outlinedTextFieldUsernameText.getText());

                if(!name.equals("") && selectedImageUri != null){


                    saveInfo(selectedImageUri,name);

                    FancyToast.makeText(getContext(),
                                    getString(R.string.success_insert),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    R.drawable.ic_baseline_check_circle_outline_24,
                                    false)
                            .show();

                    NavHostFragment.findNavController(SettingFragment.this)
                            .navigate(R.id.action_SettingFragment_to_FirstFragment);
                }else{
                    FancyToast.makeText(getContext(),
                                    getString(R.string.blank_field),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.WARNING,
                                    R.drawable.ic_baseline_error_outline_24,
                                    false)
                            .show();
                }

            }
        });




        // Initialize the ActivityResultLauncher for image picking
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // The user picked an image
                            Intent data = result.getData();
                            if (data != null) {
                                selectedImageUri = data.getData();
                                String imagePath = getPathFromUri(selectedImageUri);

                                imageView = view.findViewById(R.id.img_view); // Update with your ImageView ID
                                if (imagePath != null) {
                                    Glide.with(view)
                                            .load(selectedImageUri)
                                            .into(binding.imgView);
                                    //imageView.setImageURI(selectedImageUri);
                                }

                            }
                        }
                    }
                });

        // Example: Launch image picker when a button is clicked
        view.findViewById(R.id.button_picker).setOnClickListener(v -> pickImage());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveInfo(Uri uri, String name) {
        //String imagePath = getPathFromUri(uri);

        info_DAO infoDao = database.infoDao();
        info inf = new info();
        inf.setImagePath(uri.toString());
        inf.setId(0);
        inf.setName(name);
        if(infoDao.getItemById(0) == null){
            infoDao.insert(inf);
        }else{
            infoDao.update(inf);
        }
    }

    // Utility method to get the path from a content URI
    public String getPathFromUri(Uri uri) {
        // You can implement this method based on your needs.
        // For simplicity, this example assumes the URI is a media store URI.
        // Real-world implementation may require handling various URI schemes.
        // This example uses MediaStore.Images.Media.DATA to get the path.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}
