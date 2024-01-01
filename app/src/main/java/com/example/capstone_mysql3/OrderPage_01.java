package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderPage_01 extends AppCompatActivity {

    private ArrayList<GenericMedicine> cartMedicines = new ArrayList<>();
    RecyclerView recyclerView;
    OrderPrescribedMedicinesAdapter1 adapter;
    List<PrescribedMedicines> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page_01);

        Button addGenericMedicinesButton = findViewById(R.id.btnAddGenericMedicines);
        addGenericMedicinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGenericMedicinesAndStartActivity();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        medicineList = new ArrayList<>();
        loadDataFromIntent();

        adapter = new OrderPrescribedMedicinesAdapter1(this, medicineList, false);
        recyclerView.setAdapter(adapter);

        Button orderNowButton = findViewById(R.id.btnOrderNow);
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderNow();
            }
        });
    }

    private void loadDataFromIntent() {
        String response = getIntent().getStringExtra("medicineData");

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray medicineArray = obj.getJSONArray("medicines");

            for (int i = 0; i < medicineArray.length(); i++) {
                JSONObject medicineObject = medicineArray.getJSONObject(i);

                int id = medicineObject.getInt("medicine_id");
                String name = medicineObject.getString("medicine_name");
                float price = (float)medicineObject.getDouble("medicine_price");

                PrescribedMedicines medicine = new PrescribedMedicines(id, name, price);
                medicineList.add(medicine);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void orderNow() {
        Intent intent = new Intent(OrderPage_01.this, OrderFinalPage.class);


        Gson gson = new Gson();
        String medicineListJson = gson.toJson(medicineList);
        intent.putExtra("selectedMedicines", medicineListJson); //key match

        intent.putExtra("isFinalPage", true);
        startActivity(intent);
    }

    private void getGenericMedicinesAndStartActivity() {
        String url = constants.NON_PRESCRIPTION_MEDS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<GenericMedicine> genericMedicines = new ArrayList<>();

                        try {
                            JSONArray medicinesArray = response.getJSONArray("nonprescriptionmeds");

                            for (int i = 0; i < medicinesArray.length(); i++) {
                                JSONObject medicineObject = medicinesArray.getJSONObject(i);

                                int id = medicineObject.getInt("medicine_id");
                                String name = medicineObject.getString("medicine_name");
                                float price = (float) medicineObject.getDouble("medicine_price");
                                int defaultQuantity = 1;
                                genericMedicines.add(new GenericMedicine(id, name, price, defaultQuantity));
                            }

                            Intent intent = new Intent(OrderPage_01.this, OrderPage_02.class);

                            // medicineList to JSON String
                            Gson gson = new Gson();
                            String medicineListJson = gson.toJson(medicineList);
                            intent.putExtra("medicineList", medicineListJson);

                            intent.putExtra("genericMedicines", genericMedicines);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderPage_01.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderPage_01.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}