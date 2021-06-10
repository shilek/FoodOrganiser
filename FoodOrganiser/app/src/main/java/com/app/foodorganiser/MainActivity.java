package com.app.foodorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodorganiser.entity.ProductTable;
import com.app.foodorganiser.productpackage.AddProductActivity;
import com.app.foodorganiser.productpackage.DatabaseClass;
import com.app.foodorganiser.productpackage.ProductDisplay;
import com.app.foodorganiser.productpackage.QueryBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<ProductTable> allProductsList;
    Button products, timetable, meals, account;
    PieChart piechart;
    //FloatingActionButton connect;

    // TODO: ZABAWECZKI DREWIENKA ==================================================================
    private String[] mealsNames = new String[]{"Breakfast", "2nd breakfast", "Lunch", "Supper", "Dinner"};
    TextView proteins;
    TextView carbos;
    TextView fats;
    TextView proteinsPerMeal;
    TextView carbosPerMeal;
    TextView fatsPerMeal;
    static List<ProductTable> breakfast = new ArrayList<>();
    static List<ProductTable> breakfast2 = new ArrayList<>();
    static List<ProductTable> lunch = new ArrayList<>();
    static List<ProductTable> supper = new ArrayList<>();
    static List<ProductTable> dinner = new ArrayList<>();
    static List<Double> breakfastMacros = new ArrayList<>();
    static List<Double> breakfast2Macros;
    static List<Double> lunchMacros;
    static List<Double> supperMacros;
    static List<Double> dinnerMacros;
    static List<Double> dailyMacros;
    //TODO: Mine now ===============================================================================
    ArrayAdapter<String> mealsListAdapter;
    ListView mealList;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (allProductsList == null) allProductsList = new ArrayList<>();

        products = findViewById(R.id.add_pr_button);
        timetable = findViewById(R.id.TimetableButton);
        meals = findViewById(R.id.MealsButton);
        account = findViewById(R.id.AccountButton);
        //connect = findViewById(R.id.connectionButton);

        //Load products from database, maybe add exception when wont connect to db?
        new Thread(() -> {
            List<String> list;
            String string;
            DatabaseClass db = new  DatabaseClass();
            try {
                db.openConnection();
                db.sendQuery(QueryBuilder.buildQuery("SELECT * FROM products_table"));
                string = db.receiveReply();
                list = QueryBuilder.toList(string);
                allProductsList = ProductTable.toObject(list);
                products.setClickable(true);
                timetable.setClickable(true);
                meals.setClickable(true);
                account.setClickable(true);
                //connect.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));//setBackgroundColor(Color.GREEN);
            }
            catch (Exception ignore) {
                products.setClickable(false);
                timetable.setClickable(false);
                meals.setClickable(false);
                account.setClickable(false);
                ignore.printStackTrace();
                //connect.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        }).start();

        //==========================================================================================
        piechart = findViewById(R.id.piechart);
        proteins = (TextView)findViewById(R.id.proteins_tv);
        carbos = (TextView)findViewById(R.id.carbos_tv);
        fats = (TextView)findViewById(R.id.fats_tv);
        proteinsPerMeal = (TextView)findViewById(R.id.proteins_per_meal_tv);
        carbosPerMeal = (TextView)findViewById(R.id.carbos_per_meal_tv);
        fatsPerMeal = (TextView)findViewById(R.id.fats_per_meal_tv);

        mealList = findViewById(R.id.timeMealsList);
        mealsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealsNames);
        mealList.setAdapter(mealsListAdapter);
        //==========================================================================================

        // Load current days' meals
        //==========================================================================================
        loadMenu();
        //==========================================================================================


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
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
        });

        mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    proteinsPerMeal.setText(breakfastMacros.get(0).toString());
                    carbosPerMeal.setText(breakfastMacros.get(1).toString());
                    fatsPerMeal.setText(breakfastMacros.get(2).toString());
                }
                if (position == 1){
                    proteinsPerMeal.setText(breakfast2Macros.get(0).toString());
                    carbosPerMeal.setText(breakfast2Macros.get(1).toString());
                    fatsPerMeal.setText(breakfast2Macros.get(2).toString());
                }
                if (position == 2){
                    proteinsPerMeal.setText(lunchMacros.get(0).toString());
                    carbosPerMeal.setText(lunchMacros.get(1).toString());
                    fatsPerMeal.setText(lunchMacros.get(2).toString());
                }
                if (position == 3){
                    proteinsPerMeal.setText(supperMacros.get(0).toString());
                    carbosPerMeal.setText(supperMacros.get(1).toString());
                    fatsPerMeal.setText(supperMacros.get(2).toString());
                }
                if (position == 4){
                    proteinsPerMeal.setText(dinnerMacros.get(0).toString());
                    carbosPerMeal.setText(dinnerMacros.get(1).toString());
                    fatsPerMeal.setText(dinnerMacros.get(2).toString());
                }
            }
        });
    }

    public void loadMenu() {

        LocalDate localDate = LocalDate.now().minusMonths(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d.M.yyyy");
        String date = localDate.format(dateFormat);

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

        if (breakfast == null) {
            breakfast = new ArrayList<>();
        }
        if (breakfast2 == null) {
            breakfast2 = new ArrayList<>();
        }
        if (lunch == null) {
            lunch = new ArrayList<>();
        }
        if (supper == null) {
            supper = new ArrayList<>();
        }
        if (dinner == null) {
            dinner = new ArrayList<>();
        }

        breakfastMacros = new ArrayList<>(countMacros(breakfast));
        breakfast2Macros = new ArrayList<>(countMacros(breakfast2));
        lunchMacros = new ArrayList<>(countMacros(lunch));
        supperMacros = new ArrayList<>(countMacros(supper));
        dinnerMacros = new ArrayList<>(countMacros(dinner));
        dailyMacros = new ArrayList<>(countDailyMacros());

        proteins.setText(dailyMacros.get(1).toString());
        carbos.setText(dailyMacros.get(0).toString());
        fats.setText(dailyMacros.get(2).toString());

        pieSlices();
    }

    public void pieSlices() {
        piechart.clearChart();
        piechart.addPieSlice(
                new PieModel(
                        "Proteins",
                        (Float) dailyMacros.get(1).floatValue(),
                        Color.parseColor("#FFA726")));
        piechart.addPieSlice(
                new PieModel(
                        "Carbos",
                        (Float) dailyMacros.get(0).floatValue(),
                        Color.parseColor("#EF5350")));
        piechart.addPieSlice(
                new PieModel(
                        "Fats",
                        (Float) dailyMacros.get(2).floatValue(),
                        Color.parseColor("#29B6F6")));
        piechart.startAnimation();
    }

    static public List<Double> countDailyMacros() {
        Double carbo = 0.0, protein = 0.0, fats = 0.0;

        // TODO: PepeDance
        carbo += breakfastMacros.get(0);
        carbo += breakfast2Macros.get(0);
        carbo += lunchMacros.get(0);
        carbo += supperMacros.get(0);
        carbo += dinnerMacros.get(0);
        protein += breakfastMacros.get(1);
        protein += breakfast2Macros.get(1);
        protein += lunchMacros.get(1);
        protein += supperMacros.get(1);
        protein += dinnerMacros.get(1);
        fats += breakfastMacros.get(2);
        fats += breakfast2Macros.get(2);
        fats += lunchMacros.get(2);
        fats += supperMacros.get(2);
        fats += dinnerMacros.get(2);

        List<Double> targetList = new ArrayList<>();
        targetList.add(protein);
        targetList.add(carbo);
        targetList.add(fats);
        return targetList;
    }

    static public List<Double> countMacros(List<ProductTable> list) {
        Double carbo = 0.0, protein = 0.0, fats = 0.0;
        if(list.size() > 0)
        for(int i=0; i < list.size(); i++) {
            carbo += list.get(i).getCarbohydrates();
            protein += list.get(i).getProtein();
            fats += list.get(i).getFats();
        }
        List<Double> targetList = new ArrayList<>();
        targetList.add(protein);
        targetList.add(carbo);
        targetList.add(fats);
        return targetList;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenu();
    }
}