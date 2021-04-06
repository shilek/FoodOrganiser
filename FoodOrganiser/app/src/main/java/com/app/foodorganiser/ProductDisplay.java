package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ProductDisplay extends AppCompatActivity implements ProductListener{
    private List<ProductTable> list;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchView searchView;
    ProductAdapter productAdapter;
    DatabaseClass database;
    ProductDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_layout);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.productDisplay);
        searchView = findViewById(R.id.search_bar);

        database = Room.databaseBuilder(this, DatabaseClass.class,"AppDatabase")
                .build();
        dao = database.productDAO();
//        dao.getAll().observe(this, productTables -> {
//            list = productTables;
//            productAdapter = new ProductAdapter(list,this);
//            recyclerView.setAdapter(productAdapter);
//            recyclerView.setLayoutManager(layoutManager);
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    @Override
    public void onClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name",list.get(position).getName());
        bundle.putString("code",list.get(position).getCode());
        bundle.putString("pathPhoto",list.get(position).getPathPhoto());
        bundle.putInt("protein",list.get(position).getProtein());
        bundle.putInt("carbohydrates",list.get(position).getCarbohydrates());
        bundle.putInt("fats",list.get(position).getFats());

        ProductDescribes productDescribes = new ProductDescribes();
        productDescribes.setArguments(bundle);
        productDescribes.show(getSupportFragmentManager(),"Product");

    }
}