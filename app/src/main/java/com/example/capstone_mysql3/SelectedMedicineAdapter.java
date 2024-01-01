package com.example.capstone_mysql3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectedMedicineAdapter extends RecyclerView.Adapter<SelectedMedicineAdapter.SelectedMedicineViewHolder> {

    private Context mCtx;
    private List<Medicines> selectedMedicinesList;

    public SelectedMedicineAdapter(Context mCtx, List<Medicines> selectedMedicinesList) {
        this.mCtx = mCtx;
        this.selectedMedicinesList = selectedMedicinesList;
    }

    @NonNull
    @Override
    public SelectedMedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_selected_medicine, null);
        return new SelectedMedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedMedicineViewHolder holder, int position) {
        Medicines medicines = selectedMedicinesList.get(position);

        if (medicines != null) {
            holder.medicineIdTextView.setText(String.valueOf(medicines.getMedicine_id()));
            holder.medicineNameTextView.setText(medicines.getMedicine_name());

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedMedicinesList.remove(position);
                    notifyDataSetChanged();
                    holder.dosageEditText.setText("");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selectedMedicinesList.size();
    }

    class SelectedMedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineIdTextView, medicineNameTextView;
        EditText dosageEditText;
        Button removeButton;

        public SelectedMedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineIdTextView = itemView.findViewById(R.id.medicineIdTextView);
            medicineNameTextView = itemView.findViewById(R.id.medicineNameTextView);
            dosageEditText = itemView.findViewById(R.id.dosageEditText);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
    public interface OnMedicineRemovedListener {
        void onMedicineRemoved(int position);
    }
}