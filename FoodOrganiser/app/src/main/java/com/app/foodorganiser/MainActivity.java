package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.app.foodorganiser.entity.ProductTable;
import com.app.foodorganiser.productpackage.DatabaseClass;
import com.app.foodorganiser.productpackage.ProductDisplay;
import com.app.foodorganiser.productpackage.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<ProductTable> allProductsList;
    Button products, timetable, meals, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(allProductsList == null) allProductsList = new ArrayList<>();

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

        //Load products from database, maybe add exception when wont connect to db?
        new Thread(() -> {
            List<String> list;
            String string;
            DatabaseClass db = new  DatabaseClass();
            db.openConnection();
            db.sendQuery(QueryBuilder.buildQuery("SELECT * FROM products_table"));
            string = db.receiveReply();
            list = QueryBuilder.toList(string);
            allProductsList = ProductTable.toObject(list);
        }).start();

        products = findViewById(R.id.ProductsButton);
        timetable = findViewById(R.id.TimetableButton);
        meals = findViewById(R.id.MealsButton);
        account = findViewById(R.id.AccountButton);

        products.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductDisplay.class);
            startActivity(intent);
        });

        timetable.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimetableActivity.class);
            startActivity(intent);
        });

        meals.setOnClickListener(v -> {
            Intent intent = new Intent(this, MealsActivity.class);
            startActivity(intent);
        });

        account.setOnClickListener(v -> {
//            Intent intent = new Intent(this, ProductDisplay.class);
//            startActivity(intent);
        });
    }
}