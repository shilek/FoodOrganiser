package com.app.foodorganiser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.foodorganiser.Product;
import com.app.foodorganiser.ProductTable;
import com.app.foodorganiser.R;
import com.app.foodorganiser.TimetableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<ProductTable> products;
    ContactFilter mFilter = new ContactFilter();
    String filterString = new String();

    ProductListAdapter(Context context, List<ProductTable> products) {
        this.context = context;
        this.products = products;
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

        holder.name.setText(products.get(position).getName());
        holder.protein.setText("Protein: "+String.valueOf(products.get(position).getProtein()));
        holder.carbohydrates.setText("Carbo: "+String.valueOf(products.get(position).getCarbohydrates()));
        holder.fats.setText(String.valueOf("Fats: "+products.get(position).getFats()));

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
