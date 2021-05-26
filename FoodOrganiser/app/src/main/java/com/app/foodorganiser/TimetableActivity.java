package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.app.foodorganiser.adapters.ProductListAdapter;
import com.app.foodorganiser.entity.ProductTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TimetableActivity extends AppCompatActivity {

    private String[] meals = new String[]{"Breakfast", "2nd breakfast", "Lunch", "Supper", "Dinner"};
    static public List<ProductTable> breakfast = new ArrayList<>();
    static public List<ProductTable> breakfast2 = new ArrayList<>();
    static public List<ProductTable> lunch = new ArrayList<>();
    static public List<ProductTable> supper = new ArrayList<>();
    static public List<ProductTable> dinner = new ArrayList<>();
    private String date;
    boolean menuLoaded;
    String selectedMeal;
    Button addIngredientButton;
    ArrayAdapter<String> mealsListAdapter;
    public static ProductListAdapter productsListAdapter;
    ListView mealList;
    ListView productList;
    static TextView macroInfo;
    static List<Double> countedMacros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        mealList = findViewById(R.id.timetableMealsList);
        productList = findViewById(R.id.timetableProductsList);
        addIngredientButton = findViewById(R.id.addIngredientTimetable);
        addIngredientButton.setVisibility(View.GONE);
        macroInfo = findViewById(R.id.macroInfo);
        mealsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meals);
        //kalendarz do klikania
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendar);
        menuLoaded = false;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if(menuLoaded && date != dayOfMonth + "." + month + "." + year) saveMenu(date);
                addIngredientButton.setVisibility(View.VISIBLE);
                date = dayOfMonth + "." + month + "." + year;
                loadMenu(date);
                menuLoaded = true;
                mealList.setAdapter(mealsListAdapter);
                selectedMeal = "breakfast";
                productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast, "breakfast");
                productList.setAdapter(productsListAdapter);
                countMacros(breakfast);
                setMacroText();
            }
        });

//        mealList.setAdapter(arrayAdapter);

        //lista posilkow do klikniecia
        mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    selectedMeal = "breakfast";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast, "breakfast");
                    productList.setAdapter(productsListAdapter);
                    countMacros(breakfast);
                    setMacroText();

                }
                if (position == 1){
                    selectedMeal = "breakfast2";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast2, "breakfast2");
                    productList.setAdapter(productsListAdapter);
                    countMacros(breakfast2);
                    setMacroText();
                }
                if (position == 2){
                    selectedMeal = "lunch";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), lunch, "lunch");
                    productList.setAdapter(productsListAdapter);
                    countMacros(lunch);
                    setMacroText();
                }
                if (position == 3){
                    selectedMeal = "supper";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), supper, "supper");
                    productList.setAdapter(productsListAdapter);
                    countMacros(supper);
                    setMacroText();
                }
                if (position == 4){
                    selectedMeal = "dinner";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), dinner, "dinner");
                    productList.setAdapter(productsListAdapter);
                    countMacros(dinner);
                    setMacroText();
                }
            }
        });

        addIngredientButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PickIngredient.class);
            intent.putExtra("action", "addToTimetable");
            intent.putExtra("selectedMeal", selectedMeal);
            startActivity(intent);
        });
    }

    public void loadMenu(String date){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.app.foodorganiser." + date, Context.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.app.foodorganiser." + date, Context.MODE_PRIVATE);
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

     static public void countMacros(List<ProductTable> list){
        double carbo = 0, protein = 0, fats = 0;
        if (list.size() > 0)
        for (int i = 0; i < list.size(); i++) {
            carbo += list.get(i).getCarbohydrates();
            protein += list.get(i).getProtein();
            fats += list.get(i).getFats();
        }
        countedMacros.clear();
        countedMacros.add(protein);
        countedMacros.add(carbo);
        countedMacros.add(fats);
    }

    static public void setMacroText(){
        macroInfo.setText("Protein: " + countedMacros.get(0) + "  Carbo: " + countedMacros.get(1) + "  Fats: " + countedMacros.get(2));
    }

    @Override
    public void onBackPressed() {
        if(menuLoaded) saveMenu(date);
        finish();
    }
}