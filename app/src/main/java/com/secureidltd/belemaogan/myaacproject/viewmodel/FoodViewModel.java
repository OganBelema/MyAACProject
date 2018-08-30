package com.secureidltd.belemaogan.myaacproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.secureidltd.belemaogan.myaacproject.MyDatabase;
import com.secureidltd.belemaogan.myaacproject.model.Food;

import java.util.List;

public class FoodViewModel extends AndroidViewModel{

    private MyDatabase myDatabase;
    private LiveData<Food> foodLiveData;
    private LiveData<List<Food>> foodListLiveData;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        myDatabase = MyDatabase.getInstance(application);
    }

    public LiveData<Food> getFoodWithId(int id){
        foodLiveData = myDatabase.getFoodDoa().getFood(id);
        return foodLiveData;
    }

    public LiveData<List<Food>> getAllFoodItems(){
        foodListLiveData = myDatabase.getFoodDoa().getFoodList();
        return foodListLiveData;
    }

    public LiveData<List<Food>> getFoodInCategory(int id){
        foodListLiveData = myDatabase.getFoodDoa().getFoodItemsWithCategoryId(id);
        return foodListLiveData;
    }

    public void insertFood(final Food food){
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.getFoodDoa().insertFood(food);
            }
        }).start();

    }

    public void deleteFood(final Food food){
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.getFoodDoa().deleteFood(food);
            }
        }).start();
    }
}
