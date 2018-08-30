package com.secureidltd.belemaogan.myaacproject.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.secureidltd.belemaogan.myaacproject.MyDatabase;
import com.secureidltd.belemaogan.myaacproject.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private MyDatabase myDatabase;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        myDatabase = MyDatabase.getInstance(application);
    }

    public LiveData<Category> getCategoryWithId(int id){
        return myDatabase.getCategoryDoa().getCategoryWithId(id);
    }

    public LiveData<List<Category>> getListOfCategories(){
        return myDatabase.getCategoryDoa().getCategories();
    }

    public void insertCategory(final Category category){
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.getCategoryDoa().insertCategory(category);
            }
        }).start();
    }

    public void deleteCategory(final Category category){
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabase.getCategoryDoa().deleteCategory(category);

            }
        }).start();
    }

}
