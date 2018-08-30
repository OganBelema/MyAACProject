package com.secureidltd.belemaogan.myaacproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.secureidltd.belemaogan.myaacproject.R;
import com.secureidltd.belemaogan.myaacproject.model.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private List<Food> foodList;

    public void addFoodList(List<Food> foodList){
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    public Food getFood(int position){
        return foodList.get(position);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FoodViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.food_item_layout, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        if (foodList != null){
            Food food = foodList.get(i);
            foodViewHolder.foodNameTv.setText(food.name);
            foodViewHolder.foodPriceTv.setText(String.valueOf(food.price));
            foodViewHolder.foodImageView.setImageBitmap(getBitmapFromByteArray(food.image));
        }
    }

    @Override
    public int getItemCount() {
        if (foodList != null){
            return foodList.size();
        }
        return 0;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView foodNameTv;
        TextView foodPriceTv;
        ImageView foodImageView;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTv = itemView.findViewById(R.id.food_name_tv);
            foodPriceTv = itemView.findViewById(R.id.food_price_tv);
            foodImageView = itemView.findViewById(R.id.food_image);
        }
    }

    private Bitmap getBitmapFromByteArray(byte[] image){
        return BitmapFactory.decodeByteArray(image,0, image.length);
    }
}
