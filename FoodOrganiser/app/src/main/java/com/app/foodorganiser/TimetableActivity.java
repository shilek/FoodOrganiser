package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TimetableActivity extends AppCompatActivity {

    private String[] meals = new String[]{"Breakfast", "2nd breakfast", "Lunch", "Supper", "Dinner"};
    private List<ProductTable> breakfast = new ArrayList<>();
    private List<ProductTable> breakfast2 = new ArrayList<>();
    private List<ProductTable> lunch = new ArrayList<>();
    private List<ProductTable> supper = new ArrayList<>();
    private List<ProductTable> dinner = new ArrayList<>();

    ArrayAdapter<String> mealsListAdapter;
    ProductListAdapter productsListAdapter;
    ListView mealList;
    ListView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        mealList = findViewById(R.id.timetableMealsList);
        productList = findViewById(R.id.timetableProductsList);
        mealsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meals);
        //kalendarz do klikania
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + month + "." + year;
                loadMenu(date);
                mealList.setAdapter(mealsListAdapter);
                productList.setAdapter(null);
//                breakfast.add(new ProductTable("milk", 20, 0, 3, "123milk", null));
//                breakfast.add(new ProductTable("yoghurt", 10, 7, 5, "123yogh", null));
//                lunch.add(new ProductTable("peanut butter", 10, 30, 100, "peanut123", null));
//                saveMenu(date);
//                Toast.makeText(getApplicationContext(), breakfast.get(0).getName(), Toast.LENGTH_SHORT).show();// TODO Auto-generated method stub
            }
        });

//        mealList.setAdapter(arrayAdapter);

        //lista posilkow do klikniecia
        mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast);
                    productList.setAdapter(productsListAdapter);
                }
                if (position == 1){
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast2);
                    productList.setAdapter(productsListAdapter);
                }
                if (position == 2){
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), lunch);
                    productList.setAdapter(productsListAdapter);
                }
                if (position == 3){
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), supper);
                    productList.setAdapter(productsListAdapter);
                }
                if (position == 4){
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), dinner);
                    productList.setAdapter(productsListAdapter);
                }
            }
        });


    }

    public void loadMenu(String date){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.app.foodorganiser."+date, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductTable>>() {}.getType();

        String json = sharedPreferences.getString("breakfast", null);
        breakfast = gson.fromJson(json, type);
        json = sharedPreferences.getString("breakfast2", null);
        breakfast2 = gson.fromJson(json, type);
        json = sharedPreferences.getString("lunch", null);
        lunch = gson.fromJson(json, type);
        json = sharedPreferences.getString("supper", null);
        supper = gson.fromJson(json, type);
        json = sharedPreferences.getString("dinner", null);
        dinner = gson.fromJson(json, type);

        if(breakfast == null){
            breakfast = new ArrayList<>();
        }
        if(breakfast2 == null){
            breakfast2 = new ArrayList<>();
        }
        if(lunch == null){
            lunch = new ArrayList<>();
        }
        if(supper == null){
            supper = new ArrayList<>();
        }
        if(dinner == null){
            dinner = new ArrayList<>();
        }
    }

    public void saveMenu(String date){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.app.foodorganiser."+date, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(breakfast);
        prefsEditor.putString("breakfast", json);
        json = gson.toJson(breakfast2);
        prefsEditor.putString("breakfast2", json);
        json = gson.toJson(lunch);
        prefsEditor.putString("lunch", json);
        json = gson.toJson(supper);
        prefsEditor.putString("supper", json);
        json = gson.toJson(dinner);
        prefsEditor.putString("dinner", json);

        prefsEditor.commit();
    }
}