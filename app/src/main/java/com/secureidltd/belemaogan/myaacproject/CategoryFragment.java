package com.secureidltd.belemaogan.myaacproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.secureidltd.belemaogan.myaacproject.adapter.CategoryAdapter;
import com.secureidltd.belemaogan.myaacproject.model.Category;
import com.secureidltd.belemaogan.myaacproject.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel mCategoryViewModel;
    private ProgressBar mCategoryPb;
    private TextView mNoItemTv;
    private RecyclerView mCategoryRv;
    private CategoryAdapter mAdapter;
    private FragmentActivity fragmentActivity;

    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View categoryView = inflater.inflate(R.layout.fragment_category, container, false);

        mCategoryPb = categoryView.findViewById(R.id.category_progressbar);
        mNoItemTv = categoryView.findViewById(R.id.no_item_textView);
        fragmentActivity = this.getActivity();

        fragmentActivity.setTitle(getString(R.string.categories));

        FloatingActionButton addCategoryFab = categoryView.findViewById(R.id.add_category_fab);
        addCategoryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddCategoryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mCategoryRv = categoryView.findViewById(R.id.category_recyclerView);
        mCategoryRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mAdapter = new CategoryAdapter(fragmentActivity, new CategoryAdapter.LongClickListener() {
            @Override
            public boolean onLongClick(Category category) {
                deleteCategoryDialog(category);
                return true;
            }
        });
        mCategoryRv.setAdapter(mAdapter);

        mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        mCategoryViewModel.getListOfCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (mCategoryPb.getVisibility() == View.VISIBLE){
                    mCategoryPb.setVisibility(View.GONE);
                }
                if (categories == null || categories.size() <= 0){
                    mNoItemTv.setVisibility(View.VISIBLE);
                }

                mAdapter.addCategories(categories);
            }
        });

        return categoryView;
    }

    private void deleteCategoryDialog(final Category category){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this item?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCategoryViewModel.deleteCategory(category);
                    }
                });
        builder.show();
    }

}
