package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//    new Thread(() -> {
//
//        DatabaseClass database;
//        ProductDAO dao;
//
//        database = Room.databaseBuilder(this,DatabaseClass.class,"AppDatabase")
//                .build();
//        dao = database.productDAO();
//        dao.insert(new ProductTable("product1",1,2,3,"123456789",null));
//        dao.insert(new ProductTable("product2",3,2,3,"423456789",null));
//        dao.insert(new ProductTable("product3",2,2,3,"423456789",null));
//    }).start();


       startActivity(new Intent(this, TimetableActivity.class));
    }
}