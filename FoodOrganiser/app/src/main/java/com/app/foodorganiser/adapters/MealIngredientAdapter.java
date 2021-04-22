package com.app.foodorganiser.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.foodorganiser.CreateMeal;
import com.app.foodorganiser.R;
import com.app.foodorganiser.entity.ProductTable;

import java.util.ArrayList;
import java.util.List;

public class MealIngredientAdapter extends BaseAdapter {

    Context context;
    List<ProductTable> products = CreateMeal.getMealProductsList();

    public MealIngredientAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if(products == null) products = new ArrayList<>();

        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView ingredientName;
        TextView ingredientValues;
        ImageView delBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.meal_ingredient_item, null);
        holder = new ViewHolder();

        holder.ingredientName = convertView.findViewById(R.id.ingredient_name);
        holder.ingredientValues = convertView.findViewById(R.id.ingredient_values);
        holder.delBtn = convertView.findViewById(R.id.deleteIngredient);

        holder.ingredientName.setText("     " + products.get(position).getName());
        holder.ingredientValues.setText("      Proteins: " + products.get(position).getProtein() + "  |  Carbs: " + products.get(position).getCarbohydrates() + "  |  Fats: " + products.get(position).getFats());

        holder.delBtn.setOnClickListener(v -> {
            CreateMeal.getMealProductsList().remove(position);
            Toast.makeText(context, "Ingredient deleted", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });

        return convertView;
    }
}
