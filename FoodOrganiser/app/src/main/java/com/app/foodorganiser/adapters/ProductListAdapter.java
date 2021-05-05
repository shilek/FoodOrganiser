package com.app.foodorganiser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodorganiser.CreateMeal;
import com.app.foodorganiser.R;
import com.app.foodorganiser.TimetableActivity;
import com.app.foodorganiser.entity.ProductTable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<ProductTable> products;
    ContactFilter mFilter = new ContactFilter();
    String filterString = new String();
    String meal;


    public ProductListAdapter(Context context, List<ProductTable> products, String meal) {
        this.context = context;
        this.products = products;
        this.meal = meal;
    }

    @Override
    public int getCount() {
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

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ViewHolder {
        TextView name;
        TextView protein;
        TextView carbohydrates;
        TextView fats;
        ImageView delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(TimetableActivity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_product_timetable, null);
        holder = new ViewHolder();

        holder.name = convertView.findViewById(R.id.productNameTimetable);
        holder.protein = convertView.findViewById(R.id.productProteinTimetable);
        holder.carbohydrates = convertView.findViewById(R.id.productCarboTimetable);
        holder.fats = convertView.findViewById(R.id.productFatsTimetable);
        holder.delete = convertView.findViewById(R.id.deleteIngredientTimetable);
        holder.delete.setImageResource(R.drawable.ic_baseline_close_24);
        holder.name.setText(products.get(position).getName());
        holder.protein.setText("Protein: "+String.valueOf(products.get(position).getProtein()));
        holder.carbohydrates.setText("Carbo: "+String.valueOf(products.get(position).getCarbohydrates()));
        holder.fats.setText("Fats: "+products.get(position).getFats());
        if(meal.equals("breakfast"))
        holder.delete.setOnClickListener(v -> {
            TimetableActivity.breakfast.remove(position);
            notifyDataSetChanged();
            TimetableActivity.countMacros(TimetableActivity.breakfast);
            TimetableActivity.setMacroText();
        });
        if(meal.equals("breakfast2"))
            holder.delete.setOnClickListener(v -> {
                TimetableActivity.breakfast2.remove(position);
                notifyDataSetChanged();
                TimetableActivity.countMacros(TimetableActivity.breakfast2);
                TimetableActivity.setMacroText();
            });
        if(meal.equals("lunch"))
            holder.delete.setOnClickListener(v -> {
                TimetableActivity.lunch.remove(position);
                notifyDataSetChanged();
                TimetableActivity.countMacros(TimetableActivity.lunch);
                TimetableActivity.setMacroText();
            });
        if(meal.equals("dinner"))
            holder.delete.setOnClickListener(v -> {
                TimetableActivity.dinner.remove(position);
                notifyDataSetChanged();
                TimetableActivity.countMacros(TimetableActivity.dinner);
                TimetableActivity.setMacroText();
            });
        if(meal.equals("supper"))
            holder.delete.setOnClickListener(v -> {
                TimetableActivity.supper.remove(position);
                notifyDataSetChanged();
                TimetableActivity.countMacros(TimetableActivity.supper);
                TimetableActivity.setMacroText();
            });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    private class ContactFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<ProductTable> list = products;

            int count = list.size();
            final ArrayList<ProductTable> nList = new ArrayList<>();



            results.values = nList;
            results.count = nList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products = (ArrayList<ProductTable>) results.values;
            notifyDataSetChanged();

        }

    }
}
