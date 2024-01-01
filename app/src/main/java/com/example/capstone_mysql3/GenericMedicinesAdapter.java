package com.example.capstone_mysql3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GenericMedicinesAdapter extends RecyclerView.Adapter<GenericMedicinesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<GenericMedicine> genericMedicines;
    private List<GenericMedicine> genericMedicinesFull;
    private OnMedicineClickListener listener;
    private List<GenericMedicine> selectedMedicines;

    public GenericMedicinesAdapter(Context context, List<GenericMedicine> genericMedicines, List<GenericMedicine> selectedMedicines) {
        this.context = context;
        this.genericMedicines = genericMedicines;
        this.genericMedicinesFull = new ArrayList<>(genericMedicines);
        this.selectedMedicines = selectedMedicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_generic_medicines_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericMedicine genericMedicine = genericMedicines.get(position);
        holder.textViewTitle.setText(String.valueOf(genericMedicine.getMedicineId()));
        holder.textViewShortDesc.setText(genericMedicine.getMedicineName());
        holder.textViewPrice.setText(String.valueOf(genericMedicine.getMedicinePrice()));
    }

    @Override
    public int getItemCount() {
        return genericMedicines.size();
    }

    @Override
    public Filter getFilter() {
        return genericMedicineFilter;
    }

    private Filter genericMedicineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<GenericMedicine> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                for (GenericMedicine medicine : genericMedicinesFull) {
                    if (!selectedMedicines.contains(medicine)) {
                        filteredList.add(medicine);
                    }
                }
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (GenericMedicine medicine : genericMedicinesFull) {
                    if (medicine.getMedicineName().toLowerCase().contains(filterPattern) && !selectedMedicines.contains(medicine)) {
                        filteredList.add(medicine);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            genericMedicines.clear();
            genericMedicines.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnMedicineClickListener {
        void onMedicineClick(GenericMedicine medicine);
    }

    public void setOnMedicineClickListener(OnMedicineClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewShortDesc, textViewPrice;
        Button addMedicineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            addMedicineButton = itemView.findViewById(R.id.add_medicine_plus);

            addMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onMedicineClick(genericMedicines.get(position));
                    }
                }
            });
        }
    }
}