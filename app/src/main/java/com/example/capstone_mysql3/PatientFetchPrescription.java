package com.example.capstone_mysql3;
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientFetchPrescription extends AppCompatActivity {

    EditText editTextPhoneNumber;
    Button buttonCheckPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_fetch_prescription);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonCheckPhoneNumber = findViewById(R.id.buttonCheckPhoneNumber);

        buttonCheckPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhoneNumber();
            }
        });
    }

    private void checkPhoneNumber() {
        StringRequest request = new StringRequest(Request.Method.POST, constants.URL_Validate_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        int patientId = jsonObject.getInt("patient_id");
                        Intent intent = new Intent(PatientFetchPrescription.this, Prescription_Activity.class);
                        intent.putExtra("PATIENT_ID", patientId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PatientFetchPrescription.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientFetchPrescription.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("patient_phone", editTextPhoneNumber.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
