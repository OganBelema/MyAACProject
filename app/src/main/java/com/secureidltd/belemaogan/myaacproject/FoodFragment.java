package com.secureidltd.belemaogan.myaacproject;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.secureidltd.belemaogan.myaacproject.adapter.FoodAdapter;
import com.secureidltd.belemaogan.myaacproject.model.Category;
import com.secureidltd.belemaogan.myaacproject.model.Food;
import com.secureidltd.belemaogan.myaacproject.viewmodel.FoodViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    public static final String CATEGORY_ID = "category_id";
    private FragmentActivity fragmentActivity;
    private ProgressBar mFoodListPb;
    private TextView errorTv;

    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentActivity = getActivity();
        fragmentActivity.setTitle(getString(R.string.food_list));
        View foodView = inflater.inflate(R.layout.fragment_food, container, false);

        final FoodViewModel foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        mFoodListPb = foodView.findViewById(R.id.food_list_pb);
        errorTv = foodView.findViewById(R.id.no_item_textView);
        RecyclerView foodListRv = foodView.findViewById(R.id.food_list_rv);
        foodListRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        final FoodAdapter foodAdapter = new FoodAdapter();
        foodListRv.setAdapter(foodAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int id = bundle.getInt(CATEGORY_ID, 0);
            foodViewModel.getFoodInCategory(id).observe(this, new Observer<List<Food>>() {
                @Override
                public void onChanged(@Nullable List<Food> foods) {
                    foodAdapter.addFoodList(foods);

                    if (mFoodListPb.getVisibility() == View.VISIBLE) {
                        mFoodListPb.setVisibility(View.GONE);
                    }

                    if (foods == null || foods.size() <= 0) {
                        errorTv.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            foodViewModel.getAllFoodItems().observe(this, new Observer<List<Food>>() {
                @Override
                public void onChanged(@Nullable List<Food> foods) {
                    foodAdapter.addFoodList(foods);

                    if (mFoodListPb.getVisibility() == View.VISIBLE) {
                        mFoodListPb.setVisibility(View.GONE);
                    }

                    if (foods == null || foods.size() <= 0) {
                        errorTv.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Food food = foodAdapter.getFood(viewHolder.getAdapterPosition());
                foodViewModel.deleteFood(food);
            }
        }).attachToRecyclerView(foodListRv);


        return foodView;
    }

}
