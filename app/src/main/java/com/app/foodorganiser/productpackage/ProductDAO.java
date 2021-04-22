package com.app.foodorganiser.productpackage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.foodorganiser.entity.ProductTable;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM Products")
    LiveData<List<ProductTable>> getAll();

    @Query("SELECT * FROM Products WHERE :what LIKE :where")
    LiveData<List<ProductTable>> getByParameter(String what, String where);

    @Query("SELECT * FROM Products WHERE :what LIKE :where ORDER BY :sorting")
    LiveData<List<ProductTable>> getByParameter(String what, String where, String sorting);

    @Query("SELECT * FROM Products WHERE :what LIKE :where ORDER BY :sorting LIMIT :limit")
    LiveData<List<ProductTable>> getByParameter(String what, String where, String sorting, int limit);

    @Query("DELETE FROM Products WHERE rowid = :value")
    void delete(int value);

    @Query("DELETE FROM Products WHERE :what LIKE :value")
    void delete(String what,int value);

    @Insert
    void insert(ProductTable... productTables);

    @Delete
    void delete(ProductTable... productTable);

    @Update
    void update(ProductTable... productTables);
}
