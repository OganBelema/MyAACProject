package com.secureidltd.belemaogan.myaacproject;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.secureidltd.belemaogan.myaacproject.doa.CategoryDoa;
import com.secureidltd.belemaogan.myaacproject.doa.FoodDoa;
import com.secureidltd.belemaogan.myaacproject.model.Category;
import com.secureidltd.belemaogan.myaacproject.model.Food;

@Database(entities = {Food.class, Category.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static final String DB_NAME = "foodhub.db";
    private static MyDatabase sInstance;

    public static synchronized MyDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = create(context);
        }
        return sInstance;
    }

    private static MyDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                MyDatabase.class,
                DB_NAME).build();
    }

    public abstract CategoryDoa getCategoryDoa();
    public abstract FoodDoa getFoodDoa();
}
