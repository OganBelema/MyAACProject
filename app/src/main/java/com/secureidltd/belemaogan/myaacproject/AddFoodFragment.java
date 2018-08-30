package com.secureidltd.belemaogan.myaacproject;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.secureidltd.belemaogan.myaacproject.model.Category;
import com.secureidltd.belemaogan.myaacproject.model.Food;
import com.secureidltd.belemaogan.myaacproject.viewmodel.CategoryViewModel;
import com.secureidltd.belemaogan.myaacproject.viewmodel.FoodViewModel;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    private ImageView mFoodImageView;
    private EditText mFoodNameEt;
    private EditText mFoodPriceEt;
    private Button addFoodBtn;
    private Spinner mFoodCategorySpinner;
    private int foodCategoryId;
    private CategoryViewModel categoryViewModel;
    private FoodViewModel foodViewModel;
    private static final int CAMERA_CODE = 3;
    private static final int GALLERY_CODE = 2;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String foodName;
    private String foodPrice;
    private byte[] image;
    private Drawable defaultImageDrawable;

    public AddFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentActivity = getActivity();
        fragmentActivity.setTitle(getString(R.string.add_food));

        View addFoodView = inflater.inflate(R.layout.fragment_add_food, container, false);
        mFoodImageView = addFoodView.findViewById(R.id.food_image_iv);
        mFoodNameEt = addFoodView.findViewById(R.id.food_name_et);
        mFoodPriceEt = addFoodView.findViewById(R.id.food_price_et);
        addFoodBtn = addFoodView.findViewById(R.id.add_food_btn);
        mFoodCategorySpinner = addFoodView.findViewById(R.id.category_spinner);

        defaultImageDrawable = getResources().getDrawable(R.mipmap.insert_food_img);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        mFoodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageSelectionChooser();
            }
        });

        setupSpinner();
        handleSpinnerItemSelection();

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInput()){
                    addFood();
                }
            }
        });

        return addFoodView;
    }

    private void startImageSelectionChooser() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this.getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                requestPermission();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startGallery();
        }
    }

    private void startGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_CODE){
                Uri selectedImageUri = data.getData();
                mFoodImageView.setImageURI(selectedImageUri);
            }

            if (requestCode == CAMERA_CODE){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                mFoodImageView.setImageBitmap(thumbnail);
            }

        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);
    }

    private void setupSpinner() {
        categoryViewModel.getListOfCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    categories.add(0, new Category("Select Category"));
                    mFoodCategorySpinner.setAdapter(new ArrayAdapter<>(fragmentActivity,
                            android.R.layout.simple_spinner_dropdown_item, categories));
                }
            }
        });
    }

    private void handleSpinnerItemSelection() {
        mFoodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) mFoodCategorySpinner.getAdapter().getItem(position);
                foodCategoryId = category.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                foodCategoryId = 0;
            }
        });
    }

    private byte[] getByteArrayFromImageView(Drawable drawable){
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private Boolean verifyInput(){
        foodName = mFoodNameEt.getText().toString().trim();
        foodPrice = mFoodPriceEt.getText().toString().trim();
        image = getByteArrayFromImageView(mFoodImageView.getDrawable());
        byte[] defaultImageByteArray = getByteArrayFromImageView(defaultImageDrawable);

        if (TextUtils.isEmpty(foodName) || foodName == null){
            mFoodNameEt.setError(getString(R.string.error));
            mFoodNameEt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(foodPrice) || foodPrice == null){
            mFoodPriceEt.setError(getString(R.string.error));
            mFoodPriceEt.requestFocus();
            return false;
        }

        if (foodCategoryId == 0){
            Toast.makeText(fragmentActivity, "No food category selected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (Arrays.equals(image, defaultImageByteArray)) {
            Toast.makeText(fragmentActivity, "Please select a food image", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void addFood() {
        Food food = new Food();
        food.name = foodName;
        food.price = Integer.valueOf(foodPrice);
        food.image = image;
        food.categoryId = foodCategoryId;

        foodViewModel.insertFood(food);
        clearInput();
    }

    private void clearInput(){
        mFoodNameEt.setText("");
        mFoodPriceEt.setText("");
        mFoodImageView.setImageDrawable(defaultImageDrawable);
        mFoodCategorySpinner.setSelection(0);
    }

}
