package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.app.foodorganiser.entity.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MealsActivity extends AppCompatActivity {
    static ArrayList<Meal> mealsArray = new ArrayList<>();
    ArrayAdapter<Meal> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddMeal);
        ListView mealsList = findViewById(R.id.mealsList);
        SearchView sv = findViewById(R.id.searchView);

        //Load ArrayList of meals
        loadMeals();

        //displaying meals
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealsArray);
        mealsList.setAdapter(arrayAdapter);

        //waiting for click to edit meal
        mealsList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CreateMeal.class);
            intent.putExtra("MealId", position);
            startActivity(intent);
            finish();
        });

        //Add note fab
        fabAddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateMeal.class);
            startActivity(intent);
            finish();
        });

        // Method responsible for search view
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Static search -- searches after click
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if (notes.contains(query)) {
//                    arrayAdapter.getFilter().filter(query);
//                    arrayAdapter.notifyDataSetChanged();
//                }
//                else {
//                    toast("Match not found!");
//                }
                return false;
            }

            // Dynamic search
            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadMeals(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedMeals", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meals", null);
        Type type = new TypeToken<ArrayList<Meal>>() {
        }.getType();
        mealsArray = gson.fromJson(json, type);
        if (mealsArray == null) {
            mealsArray = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}