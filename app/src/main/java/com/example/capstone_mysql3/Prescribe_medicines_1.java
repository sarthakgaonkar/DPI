package com.example.capstone_mysql3;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Prescribe_medicines_1 extends AppCompatActivity implements MedicinesAdapter.OnMedicineSelectedListener, SelectedMedicineAdapter.OnMedicineRemovedListener {

    List<Medicines> medicinesList;
    EditText searchEditText, currentDateEditText;
    RecyclerView recyclerView, recyclerView2;
    List<Medicines> selectedMedicinesList;
    MedicinesAdapter adapter;


    @Override
    public void onMedicineSelected(Medicines medicines) {
        addMedicine(medicines);
    }

    @Override
    public void onMedicineRemoved(int position) {
        selectedMedicinesList.remove(position);
        recyclerView2.getAdapter().notifyItemRemoved(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribe_medicines_1);

        currentDateEditText = findViewById(R.id.currentDateEditText);
        currentDateEditText.setFocusable(false);
        currentDateEditText.setClickable(true);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        currentDateEditText.setText(currentDate);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = findViewById(R.id.selectedMedicinesRecyclerView);
        recyclerView2.setHasFixedSize(false);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.searchEditText);

        medicinesList = new ArrayList<>();
        selectedMedicinesList = new ArrayList();

        adapter = new MedicinesAdapter(this, medicinesList, this);
        recyclerView.setAdapter(adapter);

        SelectedMedicineAdapter selectedMedicineAdapter = new SelectedMedicineAdapter(this, selectedMedicinesList);
        recyclerView2.setAdapter(selectedMedicineAdapter);

        loadPrescriptions();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMedicines(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void gatherPrescriptionDetails(View view) {
        EditText currentDateEditText = findViewById(R.id.currentDateEditText);
        String startDate = currentDateEditText.getText().toString().trim();
        EditText expiryDateEditText = findViewById(R.id.expiryDateEditText);
        String endDate = expiryDateEditText.getText().toString().trim();
        EditText patientPhoneEditText = findViewById(R.id.patientPhone);
        String patientPhone = patientPhoneEditText.getText().toString().trim();
        EditText titleEditText = findViewById(R.id.title);
        String title = titleEditText.getText().toString().trim();
        JSONArray medicineDosages = getMedicineDosages();
        if (validateDosages(medicineDosages)) {
            DoctorSharedPrefManager doctorSharedPrefManager = DoctorSharedPrefManager.getInstance(this);
            int registrationNo = doctorSharedPrefManager.getRegistrationNo();

            if (title.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (endDate.isEmpty() || !isValidDate(endDate)) {
                Toast.makeText(this, "Invalid or empty end date. Use YYYY/MM/DD format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (medicineDosages.length() == 0) {
                Toast.makeText(this, "No medicines selected", Toast.LENGTH_SHORT).show();
                return;
            }

            if (patientPhone.isEmpty()) {
                Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.URL_INSERT_PRESCRIPTIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Prescribe_medicines_1.this, "Server Response: " + response, Toast.LENGTH_LONG).show();
                            if (response.equals("Prescription pushed successfully"))
                            {
                                Intent intent = new Intent(Prescribe_medicines_1.this, Doctor_HomePage.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Prescribe_medicines_1.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("start_date", startDate);
                    params.put("end_date", endDate);
                    params.put("patient_phone", patientPhone);
                    params.put("registration_no", String.valueOf(registrationNo));
                    params.put("title", title);
                    params.put("medicines", medicineDosages.toString());
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        } else {
            Toast.makeText(this, "Dosages cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateDosages(JSONArray medicineDosages) {
        for (int i = 0; i < medicineDosages.length(); i++) {
            try {
                JSONObject medicineObject = medicineDosages.getJSONObject(i);
                String dosage = medicineObject.optString("dosage", "");
                if (dosage.isEmpty()) {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }



    private JSONArray getMedicineDosages() {
        JSONArray medicinesArray = new JSONArray();
        for (int i = 0; i < recyclerView2.getChildCount(); i++) {
            View itemView = recyclerView2.getChildAt(i);
            EditText dosageInput = itemView.findViewById(R.id.dosageEditText);
            Medicines medicine = selectedMedicinesList.get(i);

            JSONObject medicineObject = new JSONObject();
            try {
                medicineObject.put("medicine_id", medicine.getMedicine_id());
                medicineObject.put("dosage", dosageInput.getText().toString().trim());
                medicinesArray.put(medicineObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return medicinesArray;
    }




    private void filterMedicines(String query) {
        List<Medicines> filteredList = new ArrayList<>();
        for (Medicines medicine : medicinesList) {
            if (medicine.getMedicine_name().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(medicine);
            }
        }
        adapter.filterList(filteredList);
    }

    public void addMedicine(Medicines medicines) {
        selectedMedicinesList.add(medicines);
        recyclerView2.getAdapter().notifyDataSetChanged();
    }

    private void loadPrescriptions() {
        Log.d("DEBUG", "Inside loadPrescriptions");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, constants.URL_GET_MEDICINES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DEBUG", "Response received: " + response);
                try {
                    JSONArray prescriptions = new JSONArray(response);
                    for (int i = 0; i < prescriptions.length(); i++) {
                        JSONObject prescriptionObject = prescriptions.getJSONObject(i);
                        int medicine_id = prescriptionObject.getInt("medicine_id");
                        String medicine_company = prescriptionObject.getString("medicine_company");
                        String medicine_name = prescriptionObject.getString("medicine_name");

                        Medicines medicines = new Medicines(medicine_id, medicine_company, medicine_name);
                        medicinesList.add(medicines);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("DEBUG", "JSON Parsing error: " + e.getMessage());
                    Toast.makeText(Prescribe_medicines_1.this, "Parsing error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DEBUG", "VolleyError: " + error.toString());
                Toast.makeText(Prescribe_medicines_1.this, "An error occurred", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}