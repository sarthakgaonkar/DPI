package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPage_02 extends AppCompatActivity {

    private RecyclerView recyclerView1; // for generic medicines
    private RecyclerView recyclerView2; // for prescribed+generic medicines
    private GenericMedicinesAdapter genericMedicinesAdapter;
    private SelectedGenericMedicineAdapter selectedMedicinesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page_02);

        Button addMedicinesButton = findViewById(R.id.addMedicinesButton);

        recyclerView1 = findViewById(R.id.recyclerview);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<GenericMedicine> genericMedicines = (ArrayList<GenericMedicine>) getIntent().getSerializableExtra("genericMedicines");
        String medicineListJson = getIntent().getStringExtra("medicineList");
        Gson gson = new Gson();
        Type type = new TypeToken<List<PrescribedMedicines>>() {}.getType();
        List<PrescribedMedicines> prescribedMedicines = gson.fromJson(medicineListJson, type);
        List<GenericMedicine> selectedMedicines = prescribedMedicines.stream()
                .map(pm -> new GenericMedicine(pm.getMedicineId(), pm.getMedicineName(), pm.getMedicine_price(), pm.getQuantity()))
                .collect(Collectors.toList());


        genericMedicinesAdapter = new GenericMedicinesAdapter(this, genericMedicines, selectedMedicines);
        recyclerView1.setAdapter(genericMedicinesAdapter);

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                genericMedicinesAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recyclerView2 = findViewById(R.id.selectedMedicinesRecyclerView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        selectedMedicinesAdapter = new SelectedGenericMedicineAdapter(this, selectedMedicines, medicine -> {
            genericMedicines.add(medicine);
            genericMedicinesAdapter.notifyDataSetChanged();
        });

        genericMedicinesAdapter.setOnMedicineClickListener(medicine -> {
            selectedMedicines.add(medicine);
            genericMedicines.remove(medicine);
            genericMedicinesAdapter.notifyDataSetChanged();
            selectedMedicinesAdapter.notifyDataSetChanged();
        });

        recyclerView2.setAdapter(selectedMedicinesAdapter);

        addMedicinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMedsJson = gson.toJson(selectedMedicines);
                Intent intent = new Intent(OrderPage_02.this, OrderFinalPage.class);
                intent.putExtra("selectedMedicines", selectedMedsJson);
                intent.putExtra("isFinalPage", true);
                startActivity(intent);
            }
        });
    }
}