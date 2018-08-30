package com.secureidltd.belemaogan.myaacproject.doa;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.secureidltd.belemaogan.myaacproject.model.Food;

import java.util.List;

@Dao
public interface FoodDoa {

    @Insert
    void insertFood(Food food);

    @Query("SELECT * FROM FOOD")
    LiveData<List<Food>> getFoodList();

    @Query("SELECT * FROM FOOD WHERE id = :id")
    LiveData<Food> getFood(int id);

    @Query("SELECT * FROM FOOD WHERE category_id = :categoryId")
    LiveData<List<Food>> getFoodItemsWithCategoryId(int categoryId);

    @Delete
    void deleteFood(Food food);
}
