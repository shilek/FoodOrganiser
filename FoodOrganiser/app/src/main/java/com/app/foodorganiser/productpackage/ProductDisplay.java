package com.app.foodorganiser.productpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import com.app.foodorganiser.MainActivity;
import com.app.foodorganiser.R;
import com.app.foodorganiser.adapters.ProductAdapter;
import com.app.foodorganiser.entity.ProductTable;

import java.util.List;

public class ProductDisplay extends AppCompatActivity implements ProductListener {
    private List<ProductTable> list;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchView searchView;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.allProductsList != null && MainActivity.allProductsList.size() > 0)
        {
            new Thread(() -> {
                List<String> list;
                String string;
                DatabaseClass db = new  DatabaseClass();
                try {
                    db.openConnection();
                    db.sendQuery(QueryBuilder.buildQuery("SELECT * FROM products_table"));
                    string = db.receiveReply();
                    list = QueryBuilder.toList(string);
                    MainActivity.allProductsList = ProductTable.toObject(list);
                }
                catch (Exception ignore) {
                }
            }).start();

            list = MainActivity.allProductsList;
            setContentView(R.layout.activity_product_layout);
            layoutManager = new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.productDisplay);
            searchView = findViewById(R.id.search_bar);
            productAdapter = new ProductAdapter(MainActivity.allProductsList,this);
            recyclerView.setAdapter(productAdapter);
            recyclerView.setLayoutManager(layoutManager);

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
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error");
            dialog.setMessage("Something is not quite right");
            dialog.setPositiveButton("OK", (dialog1, which) -> ProductDisplay.this.finish());
            dialog.show();
        }
    }


    @Override
    public void onClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name",list.get(position).getName());
        bundle.putString("code",list.get(position).getCode());
        bundle.putString("pathPhoto",list.get(position).getPathPhoto());
        bundle.putDouble("protein",list.get(position).getProtein());
        bundle.putDouble("carbohydrates",list.get(position).getCarbohydrates());
        bundle.putDouble("fats",list.get(position).getFats());

        ProductDescribes productDescribes = new ProductDescribes();
        productDescribes.setArguments(bundle);
        productDescribes.show(getSupportFragmentManager(),"Product");

    }
}