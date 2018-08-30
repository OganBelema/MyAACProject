package com.secureidltd.belemaogan.myaacproject;


import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.util.StringUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.secureidltd.belemaogan.myaacproject.model.Category;
import com.secureidltd.belemaogan.myaacproject.viewmodel.CategoryViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategoryFragment extends Fragment {

    private EditText mCategoryNameEt;
    private Button mCreateCategoryBtn;
    private String categoryName;
    private CategoryViewModel categoryViewModel;

    public AddCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addCategoryView = inflater.inflate(R.layout.fragment_add_category, container, false);
        mCategoryNameEt = addCategoryView.findViewById(R.id.category_name_et);
        mCreateCategoryBtn = addCategoryView.findViewById(R.id.create_category_btn);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        mCreateCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInput()){
                    insertCategoryInDB();
                    clearInput();
                }
            }
        });

        return addCategoryView;
    }

    private Boolean verifyInput(){
        categoryName = mCategoryNameEt.getText().toString().trim();

        if(TextUtils.isEmpty(categoryName)){
            mCategoryNameEt.setError(getString(R.string.error));
            mCategoryNameEt.requestFocus();
            return false;
        }

        return true;
    }

    private void insertCategoryInDB(){
        Category category = new Category(categoryName);
        categoryViewModel.insertCategory(category);
    }

    private void clearInput(){
        mCategoryNameEt.setText("");
    }

}
