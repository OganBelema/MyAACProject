package com.secureidltd.belemaogan.myaacproject.doa;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.secureidltd.belemaogan.myaacproject.model.Category;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDoa {

    @Insert(onConflict = REPLACE)
    void insertCategory(Category category);

    @Query("SELECT * FROM CATEGORY")
    LiveData<List<Category>> getCategories();

    @Query("SELECT * FROM CATEGORY WHERE id = :id")
    LiveData<Category> getCategoryWithId(int id);

    @Delete
    void deleteCategory(Category category);
}
