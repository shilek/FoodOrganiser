package com.app.foodorganiser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    private List<ProductTable> productTable;
    private List<ProductTable> productTableFull;
    private ProductListener listener;
    private Filter customFilter;


    public ProductAdapter(List<ProductTable> productTable, ProductListener listener)
    {
        if(productTable!=null)
        {
            this.productTable = productTable;
            this.productTableFull = new ArrayList<>(productTable);
            this.listener = listener;
            customFilter = setFilter();
        }
        else
            throw new NullPointerException();

    }

    public Filter setFilter()
    {
       return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ProductTable> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(productTableFull);
                } else {
                    String pattern = constraint.toString().toLowerCase().trim();
                    for (int i = 0; i < productTable.size(); i++) {
                        if (productTable.get(i).getName().toLowerCase().contains(pattern) || productTable.get(i).getCode().contains(pattern)) {
                            filteredList.add(productTable.get(i));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productTable.clear();
                productTable.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public static class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView productName;
        public TextView productCode;
        private ProductListener listener;

        public ProductHolder(@NonNull View itemView, ProductListener listener) {
            super(itemView);
            this.listener = listener;
            productName = itemView.findViewById(R.id.productNameDisplay);
            productCode = itemView.findViewById(R.id.productCodeDisplay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout_for_products,parent,false);;
        return new ProductHolder(view,listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        if(productTable!=null) {
            ProductTable productTableHolder = productTable.get(position);
            holder.productName.setText(productTableHolder.getName());
            holder.productCode.setText(productTableHolder.getCode());
        }

    }

    @Override
    public int getItemCount() {
        if(productTable != null)
            return productTable.size();
        else
            return 0;
    }

}
