package com.app.foodorganiser;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProductTable.class},version = 2,exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract ProductDAO productDAO();
}
