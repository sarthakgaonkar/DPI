package com.example.capstone_mysql3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.PrescriptionViewHolder> {

    private Context mCtx;
    private List<Medicines> medicinesList;

    public MedicinesAdapter(Context mCtx, List<Medicines> medicinesList) {
        this.mCtx = mCtx;
        this.medicinesList = medicinesList;
    }

    private OnMedicineSelectedListener listener;

    public MedicinesAdapter(Context mCtx, List<Medicines> medicinesList, OnMedicineSelectedListener listener) {
        this.mCtx = mCtx;
        this.medicinesList = medicinesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listlayout, null);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Log.d("DEBUG", "Binding data for position: " + position);

        Medicines medicines = medicinesList.get(position);

        if (medicines != null) {
            holder.textViewDoctor_name.setText(String.valueOf(medicines.getMedicine_id()));

            if (medicines.getMedicine_company() != null) {
                holder.textViewPatient_name.setText(String.valueOf(medicines.getMedicine_company()));
            }

            if (medicines.getMedicine_name() != null) {
                holder.textViewMedicine_name.setText(medicines.getMedicine_name());
            }


        }

        holder.addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicines clickedMedicines = medicinesList.get(position);
                listener.onMedicineSelected(clickedMedicines);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicinesList.size();
    }

    public interface OnMedicineSelectedListener {
        void onMedicineSelected(Medicines medicines);
    }

    public void filterList(List<Medicines> filteredList) {
        medicinesList = filteredList;
        notifyDataSetChanged();
    }


    class PrescriptionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDoctor_name, textViewMedicine_name, textViewPatient_name;
        Button addMedicineButton;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDoctor_name = itemView.findViewById(R.id.textViewTitle);
            textViewMedicine_name = itemView.findViewById(R.id.textViewShortDesc);
            textViewPatient_name = itemView.findViewById(R.id.textViewPrice);
            addMedicineButton = itemView.findViewById(R.id.add_medicine_plus);
        }
    }
}
