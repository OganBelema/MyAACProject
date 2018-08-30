package com.secureidltd.belemaogan.myaacproject.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = { @ForeignKey(entity = Category.class,
                                    parentColumns = "id",
                                    childColumns = "category_id",
                                    onDelete = CASCADE)},
        tableName = "food", indices = {@Index(value = "category_id")})
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "category_id")
    public int categoryId;
    public String name;
    public int price;
    public byte[] image;
}
