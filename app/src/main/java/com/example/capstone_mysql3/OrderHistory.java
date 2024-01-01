package com.example.capstone_mysql3;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Orders> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchOrders();
    }

    private void fetchOrders() {
        int patientId = SharedPrefManager.getInstance(this).getPatientId();
        String url = constants.FETCH_ORDERS + "?patient_id=" + patientId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            orders.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Orders order = new Orders();
                                order.setOrder_id(jsonObject.getInt("order_id"));
                                order.setPatient_name(jsonObject.getString("patient_name"));
                                order.setOrder_date(jsonObject.getString("order_date"));
                                order.setShipping_address(jsonObject.getString("shipping_address"));
                                order.setMedicine_name(jsonObject.getString("medicine_name"));
                                order.setTotal(jsonObject.getInt("total"));
                                orders.add(order);
                            }
                            if (ordersAdapter == null) {
                                ordersAdapter = new OrdersAdapter(OrderHistory.this, orders);
                                ordersRecyclerView.setAdapter(ordersAdapter);
                            } else {
                                ordersAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(OrderHistory.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("JSON Parsing Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderHistory.this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Volley Error", error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}