package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.app.foodorganiser.entity.ProductTable;

import java.sql.Time;

public class PickIngredient extends AppCompatActivity {

    ArrayAdapter<ProductTable> ptArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ingredient);
        SearchView sv = findViewById(R.id.ingrSearch);
        ListView ingrs = findViewById(R.id.ingrList);

        ptArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MainActivity.allProductsList);
        ingrs.setAdapter(ptArrayAdapter);

        if (getIntent().getStringExtra("action").equals("addToMeal")) {
            //Adding ingredient to meal list on click
            ingrs.setOnItemClickListener((parent, view, position, id) -> {
                CreateMeal.mealProductsList.add(MainActivity.allProductsList.get(position));
                finish();
            });
        }

        if (getIntent().getStringExtra("action").equals("addToTimetable")){
            ingrs.setOnItemClickListener((parent, view, position, id) -> {
                if(getIntent().getStringExtra("selectedMeal").equals("breakfast")) {
                    TimetableActivity.breakfast.add(MainActivity.allProductsList.get(position));
                    TimetableActivity.countMacros(TimetableActivity.breakfast);
                }
                if(getIntent().getStringExtra("selectedMeal").equals("breakfast2")) {
                    TimetableActivity.breakfast2.add(MainActivity.allProductsList.get(position));
                    TimetableActivity.countMacros(TimetableActivity.breakfast2);
                }
                if(getIntent().getStringExtra("selectedMeal").equals("lunch")) {
                    TimetableActivity.lunch.add(MainActivity.allProductsList.get(position));
                    TimetableActivity.countMacros(TimetableActivity.lunch);
                }
                if(getIntent().getStringExtra("selectedMeal").equals("supper")) {
                    TimetableActivity.supper.add(MainActivity.allProductsList.get(position));
                    TimetableActivity.countMacros(TimetableActivity.supper);
                }
                if(getIntent().getStringExtra("selectedMeal").equals("dinner")) {
                    TimetableActivity.dinner.add(MainActivity.allProductsList.get(position));
                    TimetableActivity.countMacros(TimetableActivity.dinner);
                }
                TimetableActivity.setMacroText();
                finish();
                TimetableActivity.productsListAdapter.notifyDataSetChanged();
            });
        }

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
                ptArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}