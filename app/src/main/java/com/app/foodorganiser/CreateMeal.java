package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.foodorganiser.adapters.MealIngredientAdapter;
import com.app.foodorganiser.entity.Meal;
import com.app.foodorganiser.entity.ProductTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateMeal extends AppCompatActivity {

    List<Meal> allMeals = new ArrayList<>();
    public static List<ProductTable> mealProductsList = new ArrayList<>();
    String newName, newMainIngr, newDescr;
    int fatValue = 0, carboValue = 0, proteinValue = 0, mealID;
    MealIngredientAdapter ingredientsAdapter;
    TextView proteinCounter;
    TextView carboCounter;
    TextView fatCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);
        FloatingActionButton addBtn = findViewById(R.id.fabMealDone);
        FloatingActionButton delBtn = findViewById(R.id.fabMealDelete);
        Button addIngrBtn = findViewById(R.id.addIngredientBtn);
        EditText mealName = findViewById(R.id.meal_title);
        EditText mealMainIngr = findViewById(R.id.meal_mainIngr);
        EditText mealDesc = findViewById(R.id.meal_desc);
        ListView ingredientsList = findViewById(R.id.ingredients_list);
        LinearLayout proteinsLayout = findViewById(R.id.proteinsLayout);
        LinearLayout carbsLayout = findViewById(R.id.carbsLayout);
        LinearLayout fatsLayout = findViewById(R.id.fatsLayout);
        mealID = getIntent().getIntExtra("MealId", -1);
        fatCounter = findViewById(R.id.fatCounter);
        carboCounter = findViewById(R.id.carbohydratesCounter);
        proteinCounter = findViewById(R.id.proteinCounter);

        loadMeals();

        ingredientsAdapter = new MealIngredientAdapter(this);
        ingredientsList.setAdapter(ingredientsAdapter);

//        //Load products from database, maybe add exception when wont connect to db?
//        List<String> list;
//        String string;
//        DatabaseClass db = new  DatabaseClass();
//        db.openConnection();
//        db.sendQuery(QueryBuilder.buildQuery("SELECT * FROM products_table"));
//        string = db.receiveReply();
//        list = QueryBuilder.toList(string);
//        allProductsList = ProductTable.toObject(list);

        //saved meal content
        if(mealID > -1){
            mealName.setText(allMeals.get(mealID).getMealName());
            mealMainIngr.setText(allMeals.get(mealID).getMealMainIngr());
            mealDesc.setText(allMeals.get(mealID).getMealDescription());

            //searching through all products for meal ingredients | making used products list
            if(allMeals.get(mealID).getProductsIDs() != null){
                for(String prodID : allMeals.get(mealID).getProductsIDs()){
                    for (ProductTable p : MainActivity.allProductsList) {
                        if (Integer.parseInt(prodID) == p.getId()){
                            mealProductsList.add(p);
                            proteinValue += p.getProtein();
                            carboValue += p.getCarbohydrates();
                            fatValue += p.getFats();
                        }
                    }
                }
                proteinCounter.setText(String.valueOf(proteinValue));
                carboCounter.setText(String.valueOf(carboValue));
                fatCounter.setText(String.valueOf(fatValue));
            } else {
                proteinCounter.setText("0");
                carboCounter.setText("0");
                fatCounter.setText("0");
            }

        } else{
            proteinCounter.setText("0");
            carboCounter.setText("0");
            fatCounter.setText("0");
        }

        //Update values after click
        proteinsLayout.setOnClickListener(v -> updateCounters());
        carbsLayout.setOnClickListener(v -> updateCounters());
        fatsLayout.setOnClickListener(v -> updateCounters());

        addIngrBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PickIngredient.class);
            startActivity(intent);
        });

        addBtn.setOnClickListener(v -> {
            String prodsIDs = null;
            if(addMeal(mealName)){
                //if it's edited, overwrite, else add new one
                if(mealID > -1){
                    allMeals.get(mealID).setMealName(mealName.getText().toString());
                    allMeals.get(mealID).setMealMainIngr(mealMainIngr.getText().toString());
                    allMeals.get(mealID).setMealDescription(mealDesc.getText().toString());
                    for(int i = 0; i < mealProductsList.size(); i++){
                        if(i == 0) prodsIDs = String.valueOf(mealProductsList.get(i).getId());
                        else prodsIDs += "," + mealProductsList.get(i).getId();
                    }
                    allMeals.get(mealID).setProductsIDs(prodsIDs);
                } else{
                    for(int i = 0; i < mealProductsList.size(); i++){
                        if(i == 0) prodsIDs = String.valueOf(mealProductsList.get(i).getId());
                        else prodsIDs += "," + mealProductsList.get(i).getId();
                    }
                    allMeals.add(new Meal(mealName.getText().toString(), mealMainIngr.getText().toString(), mealDesc.getText().toString(), prodsIDs));
                }
                saveMeals();
                Toast.makeText(this, "Meal saved!", Toast.LENGTH_SHORT).show();
                mealProductsList.clear();
                Intent intent = new Intent(this, Meals.class);
                startActivity(intent);
                //finish();
            }
        });

        delBtn.setOnClickListener(v -> {
            // if it's edited meal, delete
            if(mealID > -1){
                allMeals.remove(mealID);
                saveMeals();
            }

            Toast.makeText(this, "Meal deleted!", Toast.LENGTH_SHORT).show();
            mealProductsList.clear();
            Intent intent = new Intent(this, Meals.class);
            startActivity(intent);
        });

    }

    private boolean addMeal(EditText nameOfMeal){
        boolean flag = true;
        if(TextUtils.isEmpty(nameOfMeal.getText())){
            Toast.makeText(this, "Contact name cannot be empty!", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    public static List<ProductTable> getMealProductsList(){
        return mealProductsList;
    }

//    public static void updateMealProductList(List<ProductTable> p){
//        mealProductsList.clear();
//        mealProductsList.addAll(p);
//    }

    private void updateCounters(){
        fatValue = 0; carboValue = 0; proteinValue = 0;

        for(ProductTable p : mealProductsList){
            proteinValue += p.getProtein();
            carboValue += p.getCarbohydrates();
            fatValue += p.getFats();
        }

        proteinCounter.setText(String.valueOf(proteinValue));
        carboCounter.setText(String.valueOf(carboValue));
        fatCounter.setText(String.valueOf(fatValue));
    }

    private void saveMeals(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedMeals", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allMeals);
        prefsEditor.putString("meals", json);
        prefsEditor.apply();
    }

    private void loadMeals(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedMeals", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("meals", null);
        Type type = new TypeToken<ArrayList<Meal>>() {
        }.getType();
        allMeals = gson.fromJson(json, type);
        if (allMeals == null) {
            allMeals = new ArrayList<>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ingredientsAdapter.notifyDataSetChanged();
        updateCounters();
    }

    @Override
    public void onBackPressed(){
        mealProductsList.clear();
        Intent intent = new Intent(this, Meals.class);
        startActivity(intent);
        //finish();
    }
}