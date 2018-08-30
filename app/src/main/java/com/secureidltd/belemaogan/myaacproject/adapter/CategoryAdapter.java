package com.secureidltd.belemaogan.myaacproject.adapter;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secureidltd.belemaogan.myaacproject.AddCategoryFragment;
import com.secureidltd.belemaogan.myaacproject.FoodFragment;
import com.secureidltd.belemaogan.myaacproject.R;
import com.secureidltd.belemaogan.myaacproject.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private List<Category> categories;
    private FragmentActivity fragmentActivity;
    private LongClickListener longClickListener;

    public CategoryAdapter(FragmentActivity fragmentActivity, LongClickListener longClickListener) {
        this.fragmentActivity = fragmentActivity;
        this.longClickListener = longClickListener;
    }

    public void addCategories(List<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    public interface LongClickListener{
        boolean onLongClick(Category category);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.category_item_view, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        if (categories != null){
            final Category category = categories.get(i);
            categoryViewHolder.categoryNameTv.setText(category.name);

            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: create intent for food activity
                    Bundle bundle = new Bundle();
                    bundle.putInt(FoodFragment.CATEGORY_ID, category.id);
                    FoodFragment foodFragment = new FoodFragment();
                    foodFragment.setArguments(bundle);
                    fragmentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, foodFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            categoryViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   return longClickListener.onLongClick(category);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (categories != null){
            return categories.size();
        }
        return 0;
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView categoryNameTv;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTv = itemView.findViewById(R.id.category_name);
        }
    }
}
