package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.app.foodorganiser.entity.ProductTable;

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

        //Adding ingredient to meal list on click
        ingrs.setOnItemClickListener((parent, view, position, id) -> {
            CreateMeal.mealProductsList.add(MainActivity.allProductsList.get(position));
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