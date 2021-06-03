package com.app.foodorganiser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.app.foodorganiser.adapters.ProductListAdapter;
import com.app.foodorganiser.entity.ProductTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TimetableActivity extends AppCompatActivity {

    private String[] meals = new String[]{"Breakfast", "2nd breakfast", "Lunch", "Supper", "Dinner"};
    static private String[] hours = new String[]{"8:00", "11:00", "15:00", "18:00", "21:00"};
    static public List<ProductTable> breakfast = new ArrayList<>();
    static public List<ProductTable> breakfast2 = new ArrayList<>();
    static public List<ProductTable> lunch = new ArrayList<>();
    static public List<ProductTable> supper = new ArrayList<>();
    static public List<ProductTable> dinner = new ArrayList<>();
    private String date;
    boolean menuLoaded;
    static String selectedMeal;
    Button addIngredientButton;
    static CheckBox notificationCheckBox;
    static TextView timePicker;
    ArrayAdapter<String> mealsListAdapter;
    public static ProductListAdapter productsListAdapter;
    ListView mealList;
    ListView productList;
    static TextView macroInfo;
    static List<Double> countedMacros = new ArrayList<>();
    int hour, minute;
    String checked;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_timetable);
        mealList = findViewById(R.id.timetableMealsList);
        productList = findViewById(R.id.timetableProductsList);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setVisibility(View.GONE);
        addIngredientButton = findViewById(R.id.addIngredientTimetable);
        addIngredientButton.setVisibility(View.GONE);
        notificationCheckBox = findViewById(R.id.notificationCheckBox);
        notificationCheckBox.setVisibility(View.GONE);
        macroInfo = findViewById(R.id.macroInfo);
        mealsListAdapter = new ArrayAdapter<>(this, R.layout.custom_meals_list, R.id.textView1, meals);
        //kalendarz do klikania
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendar);
        menuLoaded = false;
        loadTime();
        if(checked != null)
        if (checked.equals("true")) notificationCheckBox.setChecked(true);
        else notificationCheckBox.setChecked(false);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if(menuLoaded && date != dayOfMonth + "." + month + "." + year) saveMenu(date);
                addIngredientButton.setVisibility(View.VISIBLE);
                notificationCheckBox.setVisibility((View.VISIBLE));
                timePicker.setVisibility((View.VISIBLE));
                date = dayOfMonth + "." + month + "." + year;
                loadMenu(date);
                menuLoaded = true;
                mealList.setAdapter(mealsListAdapter);
                selectedMeal = "breakfast";
                productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast, "breakfast");
                productList.setAdapter(productsListAdapter);
                countMacros(breakfast);
                setMacroText();
                setTimeText();
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
                    setTimeText();
                }
                if (position == 1){
                    selectedMeal = "breakfast2";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), breakfast2, "breakfast2");
                    productList.setAdapter(productsListAdapter);
                    countMacros(breakfast2);
                    setMacroText();
                    setTimeText();
                }
                if (position == 2){
                    selectedMeal = "lunch";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), lunch, "lunch");
                    productList.setAdapter(productsListAdapter);
                    countMacros(lunch);
                    setMacroText();
                    setTimeText();
                }
                if (position == 3){
                    selectedMeal = "supper";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), supper, "supper");
                    productList.setAdapter(productsListAdapter);
                    countMacros(supper);
                    setMacroText();
                    setTimeText();
                }
                if (position == 4){
                    selectedMeal = "dinner";
                    productsListAdapter = new ProductListAdapter(getApplicationContext(), dinner, "dinner");
                    productList.setAdapter(productsListAdapter);
                    countMacros(dinner);
                    setMacroText();
                    setTimeText();
                }
            }
        });

        addIngredientButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PickIngredient.class);
            intent.putExtra("action", "addToTimetable");
            intent.putExtra("selectedMeal", selectedMeal);
            startActivity(intent);
        });

        notificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        checkBoxAction();
                        saveTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        );

        timePicker.setOnClickListener(v ->{
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    TimetableActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int _hour, int _minute) {
                                hour = _hour;
                                minute = _minute;
                                if(minute < 10){
                                    timePicker.setText(hour + ":0" + minute);
                                    setHour(hour + ":0" + minute);
                                }
                                else {
                                    timePicker.setText(hour + ":" + minute);
                                    setHour(hour + ":" + minute);
                                }
                            saveTime();
                            try {
                                checkBoxAction();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 12, 0, true
            );
            timePickerDialog.updateTime(hour, minute);
            timePickerDialog.show();
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

    public void loadTime(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.app.foodorganiser.hours", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("hours", null) != null) {
            checked = sharedPreferences.getString("checkBox", null);
            hours = sharedPreferences.getString("hours", null).split(",");
        }
    }

    public void saveMenu(String date){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.app.foodorganiser." + date, Context.MODE_PRIVATE);
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

    public void saveTime(){
        setHour(timePicker.getText().toString());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.app.foodorganiser.hours", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hours.length; i++) {
            sb.append(hours[i]).append(",");
        }
        prefsEditor.putString("hours", sb.toString());
        if(notificationCheckBox.isChecked()) prefsEditor.putString("checkBox", "true");
        else prefsEditor.putString("checkBox", "false");
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

    static public void setTimeText(){
        if(selectedMeal.equals("breakfast")){
            timePicker.setText(hours[0]);
        }
        if(selectedMeal.equals("breakfast2")){
            timePicker.setText(hours[1]);
        }
        if(selectedMeal.equals("lunch")){
            timePicker.setText(hours[2]);
        }
        if(selectedMeal.equals("supper")){
            timePicker.setText(hours[3]);
        }
        if(selectedMeal.equals("dinner")){
            timePicker.setText(hours[4]);
        }
    }

    static public void setHour(String str){
        if(selectedMeal.equals("breakfast")){
            hours[0] = str;
        }
        if(selectedMeal.equals("breakfast2")){
            hours[1] = str;
        }
        if(selectedMeal.equals("lunch")){
            hours[2] = str;
        }
        if(selectedMeal.equals("supper")){
            hours[3] = str;
        }
        if(selectedMeal.equals("dinner")){
            hours[4] = str;
        }
    }

    static public void setMacroText(){
        macroInfo.setText("Protein: " + countedMacros.get(0) + "  Carbo: " + countedMacros.get(1) + "  Fats: " + countedMacros.get(2));
    }

    public void checkBoxAction() throws ParseException {
        for(int i=0; i < hours.length; i++) {
            String[] time = hours[i].split(":");
            Calendar date = Calendar.getInstance();
            date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            date.set(Calendar.MINUTE, Integer.parseInt(time[1]));

                //for(int j = 0; j < 365; j++)
                if (!notificationCheckBox.isChecked()) {
                    Intent intent = new Intent(TimetableActivity.this, AlarmReceiver.class);
                    intent.putExtra("id", i * 50);
                    intent.putExtra("title", "Food reminder");
                    intent.putExtra("text", "Eat " + meals[i] + "!");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(TimetableActivity.this, i * 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    checked = "false";
                } else {
                    if(Calendar.getInstance().getTimeInMillis() < date.getTimeInMillis() + 1000) {
                        Intent intent = new Intent(TimetableActivity.this, AlarmReceiver.class);
                        intent.putExtra("id", i * 50);
                        intent.putExtra("title", "Food reminder");
                        intent.putExtra("text", "Eat " + meals[i] + "!");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(TimetableActivity.this, i * 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                        intent = new Intent(TimetableActivity.this, AlarmReceiver.class);
                        intent.putExtra("id", i * 50);
                        intent.putExtra("title", "Food reminder");
                        intent.putExtra("text", "Eat " + meals[i] + "!");
                        pendingIntent = PendingIntent.getBroadcast(TimetableActivity.this, i * 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
                        System.out.println(Calendar.getInstance().getTimeInMillis());
                        System.out.println(date.getTimeInMillis());
                        checked = "true";
                        //date.add(Calendar.HOUR_OF_DAY, 24);
                    }
                }
            }
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        CharSequence name = "Notification channel";
        String description = "Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notification", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onBackPressed() {
        if(menuLoaded) {
            saveMenu(date);
        }
        finish();
    }
}