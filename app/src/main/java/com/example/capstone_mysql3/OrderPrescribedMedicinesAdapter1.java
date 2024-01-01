package com.example.capstone_mysql3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.List;

public class OrderPrescribedMedicinesAdapter1 extends RecyclerView.Adapter<OrderPrescribedMedicinesAdapter1.PrescribedMedicinesViewHolder> {

    private Context mCtx;
    private List<PrescribedMedicines> prescribedMedicinesList;
    private boolean isFinalPage;


    public OrderPrescribedMedicinesAdapter1(Context mCtx, List<PrescribedMedicines> prescribedMedicinesList,  boolean isFinalPage) {
        this.mCtx = mCtx;
        this.prescribedMedicinesList = prescribedMedicinesList;
        this.isFinalPage = isFinalPage;
    }

    @NonNull
    @Override
    public PrescribedMedicinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.activity_order_prescribed_medicines, parent, false);
        return new PrescribedMedicinesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescribedMedicinesViewHolder holder, int position) {

        if(isFinalPage) {
            holder.decreaseButton.setVisibility(View.GONE);
            holder.increaseButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
        } else {
            holder.decreaseButton.setVisibility(View.VISIBLE);
            holder.increaseButton.setVisibility(View.VISIBLE);
            holder.removeButton.setVisibility(View.VISIBLE);
        }

        PrescribedMedicines medicines = prescribedMedicinesList.get(position);
        holder.medicineIdTextView.setText(String.valueOf(medicines.getMedicineId()));
        holder.medicineNameTextView.setText(medicines.getMedicineName());
        holder.qtyTextView.setText(String.valueOf(medicines.getQuantity()));
//        holder.tvTotalPrice.setText(String.valueOf(medicines.getMedicine_price()));
        double totalMedicinePrice = medicines.getQuantity() * medicines.getMedicine_price();
        holder.tvTotalPrice.setText(String.valueOf(totalMedicinePrice));

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = medicines.getQuantity();
                if (currentQuantity > 1) {
                    currentQuantity--;
                    medicines.setQuantity(currentQuantity);

                    double updatedTotalPrice = currentQuantity * medicines.getMedicine_price();
                    holder.tvTotalPrice.setText(String.format("%.2f", updatedTotalPrice));

                    notifyItemChanged(position);
                }
            }
        });

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = medicines.getQuantity();
                currentQuantity++;
                medicines.setQuantity(currentQuantity);

                double updatedTotalPrice = currentQuantity * medicines.getMedicine_price();
                holder.tvTotalPrice.setText(String.format("%.2f", updatedTotalPrice));

                notifyItemChanged(position);
            }
        });


        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescribedMedicinesList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescribedMedicinesList.size();
    }

    public List<PrescribedMedicines> getPrescribedMedicinesList() {
        return prescribedMedicinesList;
    }

    static class PrescribedMedicinesViewHolder extends RecyclerView.ViewHolder {
        TextView medicineIdTextView, medicineNameTextView, qtyTextView, tvTotalPrice;

        Button decreaseButton, increaseButton, removeButton;

        public PrescribedMedicinesViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineIdTextView = itemView.findViewById(R.id.tvMedID);
            medicineNameTextView = itemView.findViewById(R.id.tvMedName);
            qtyTextView = itemView.findViewById(R.id.tvQuantity);
            decreaseButton = itemView.findViewById(R.id.btnDecrease);
            increaseButton = itemView.findViewById(R.id.btnIncrease);
            removeButton = itemView.findViewById(R.id.btnRemove);
            tvTotalPrice= itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}