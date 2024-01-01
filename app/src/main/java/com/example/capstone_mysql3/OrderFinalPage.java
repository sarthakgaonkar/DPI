package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFinalPage extends AppCompatActivity {

    private RecyclerView selectedMedicinesRecyclerView;
    private OrderPrescribedMedicinesAdapter1 adapter;
    private TextView totalPriceTextView, patientNameTextView;
    private EditText etAddress;
    private Button btnOrderNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_final_page);

        selectedMedicinesRecyclerView = findViewById(R.id.recyclerView);
        selectedMedicinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        patientNameTextView = findViewById(R.id.patientName);
        String patientName = SharedPrefManager.getInstance(this).getPatientName();
        patientNameTextView.setText("Name: " + patientName);

        String selectedMedicinesJson = getIntent().getStringExtra("selectedMedicines");
        Gson gson = new Gson();
        Type type = new TypeToken<List<PrescribedMedicines>>() {
        }.getType();
        List<PrescribedMedicines> selectedMedicinesList = gson.fromJson(selectedMedicinesJson, type);

        boolean isFinalPage = getIntent().getBooleanExtra("isFinalPage", false);


        double total = 0.0;
        for (PrescribedMedicines medicine : selectedMedicinesList) {
            total += medicine.getMedicine_price() * medicine.getQuantity();
        }

        totalPriceTextView.setText(String.format("Total Price: ₹%.2f", total));

        adapter = new OrderPrescribedMedicinesAdapter1(this, selectedMedicinesList, isFinalPage);
        selectedMedicinesRecyclerView.setAdapter(adapter);

        etAddress = findViewById(R.id.etAddress);
        btnOrderNow = findViewById(R.id.btnOrderNow);

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToServer();
            }
        });
    }

    private void sendOrderToServer() {
        int patientId = SharedPrefManager.getInstance(this).getPatientId();
        String address = etAddress.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address.length() <= 20) {
            Toast.makeText(this, "Please enter an address with more than 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray medicinesArray = new JSONArray();

        for (PrescribedMedicines medicine : adapter.getPrescribedMedicinesList()) {
            JSONObject medicineObject = new JSONObject();
            try {
                medicineObject.put("medicine_id", medicine.getMedicineId());
                medicineObject.put("quantity", medicine.getQuantity());
                medicinesArray.put(medicineObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String url = constants.INSERT_ORDERS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(OrderFinalPage.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(OrderFinalPage.this, PatientHomePage.class);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(OrderFinalPage.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e("VolleyError", "Response body: " + responseBody);
                        }
                        Toast.makeText(OrderFinalPage.this, "Error placing order. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_id", String.valueOf(patientId));
                params.put("shipping_address", address);
                params.put("medicines", medicinesArray.toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}