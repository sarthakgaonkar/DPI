package com.example.capstone_mysql3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class SelectedGenericMedicineAdapter extends RecyclerView.Adapter<SelectedGenericMedicineAdapter.ViewHolder> {

    private Context context;
    private List<GenericMedicine> selectedMedicines;
    private OnMedicineRemoveListener removeListener;

    public SelectedGenericMedicineAdapter(Context context, List<GenericMedicine> selectedMedicines, OnMedicineRemoveListener removeListener) {
        this.context = context;
        this.selectedMedicines = selectedMedicines;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.selected_generic_medicines, parent, false);
        return new ViewHolder(v, removeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericMedicine genericMedicine = selectedMedicines.get(position);
        holder.textViewTitle.setText(String.valueOf(genericMedicine.getMedicineId()));
        holder.textViewShortDesc.setText(genericMedicine.getMedicineName());
        holder.textViewPrice.setText(String.format(Locale.getDefault(), "%.2f", genericMedicine.getMedicinePrice()));
        holder.textViewQuantity.setText(String.valueOf(genericMedicine.getQuantity()));
        float totalPrice = genericMedicine.getQuantity() * genericMedicine.getMedicinePrice();
        holder.textViewTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
    }

    public interface OnMedicineRemoveListener {
        void onMedicineRemove(GenericMedicine medicine);
    }

    @Override
    public int getItemCount() {
        return selectedMedicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewShortDesc, textViewPrice, textViewQuantity, textViewTotalPrice;
        Button removeMedicineButton, increaseMedicineButton, decreaseMedicineButton;
        ;

        public ViewHolder(@NonNull View itemView, final OnMedicineRemoveListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tvMedID);
            textViewShortDesc = itemView.findViewById(R.id.tvMedName);
            textViewPrice = itemView.findViewById(R.id.tvTotalQuantity);
            removeMedicineButton = itemView.findViewById(R.id.removeButton);
            textViewQuantity = itemView.findViewById(R.id.tvQuantity);
            textViewTotalPrice = itemView.findViewById(R.id.tvTotalQuantity);
            increaseMedicineButton = itemView.findViewById(R.id.btnIncrease);
            decreaseMedicineButton = itemView.findViewById(R.id.btnDecrease);

            increaseMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        GenericMedicine medicine = selectedMedicines.get(position);
                        medicine.setQuantity(medicine.getQuantity() + 1);
                        float updatedTotalPrice = medicine.getQuantity() * medicine.getMedicinePrice();
                        textViewTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", updatedTotalPrice));
                        textViewQuantity.setText(String.valueOf(medicine.getQuantity()));
                    }
                }
            });

            decreaseMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        GenericMedicine medicine = selectedMedicines.get(position);
                        if (medicine.getQuantity() > 1) {
                            medicine.setQuantity(medicine.getQuantity() - 1);
                            float updatedTotalPrice = medicine.getQuantity() * medicine.getMedicinePrice();
                            textViewTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", updatedTotalPrice));
                            textViewQuantity.setText(String.valueOf(medicine.getQuantity()));
                        }
                    }
                }
            });


            removeMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onMedicineRemove(selectedMedicines.get(position));
                        selectedMedicines.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}