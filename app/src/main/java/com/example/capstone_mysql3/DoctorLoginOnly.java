//package com.example.capstone_mysql3;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.google.android.material.textfield.TextInputEditText;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.util.HashMap;
//import java.util.Map;
//
//public class DoctorLoginOnly extends AppCompatActivity implements View.OnClickListener {
//
//    private TextInputEditText etRegNo, etPhone, etPassword, etCc;
//    private Button loginDoctor;
//    private ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_doctor_login_only);
//
//        etRegNo = findViewById(R.id.etRegno);
//        etCc = findViewById(R.id.etCc);
//        etPhone = findViewById(R.id.etPhone_doc);
//        etPassword = findViewById(R.id.etPassword);
//        loginDoctor = findViewById(R.id.loginDoctor);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait...");
//
//        loginDoctor.setOnClickListener(this);
//    }
//
//
//    private void doctorLogin() {
//        final String registration = etRegNo.getText().toString().trim();
//        final String cc = etCc.getText().toString().trim();
//        final String phone_no = etPhone.getText().toString().trim();
//        final String password = etPassword.getText().toString().trim();
//        final String phone = cc + phone_no;
//
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                constants.URL_DOCTOR_LOGIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//
//                        // Check if the response contains "Connection successful" and remove it
//                        if (response.startsWith("Connection successful")) {
//                            response = response.replace("Connection successful", "");
//                        }
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            if (!obj.getBoolean("error")) {
//                                DoctorSharedPrefManager.getInstance(getApplicationContext())
//                                        .doctorLogin(
//                                                obj.getString("registration_no"),
//                                                obj.getString("doctor_name"),
//                                                obj.getString("doctor_specialization"),
//                                                obj.getString("clinic_address"),
//                                                obj.getString("doctor_phone")
//                                        );
//
//                                Intent intent = new Intent(getApplicationContext(), Doctor_HomePage.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Toast.makeText(
//                                        getApplicationContext(),
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    "JSON parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG
//                            ).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(
//                                getApplicationContext(),
//                                error.getMessage(),
//                                Toast.LENGTH_LONG
//                        ).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("registration_no", registration);
//                params.put("doctor_phone", phone);
//                params.put("doctor_password", password);
//                return params;
//            }
//        };
//
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        if (view == loginDoctor) {
//            doctorLogin();
//        }
//    }
//}
//

package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DoctorLoginOnly extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etRegNo, etPhone, etPassword, etCc;
    private Button loginDoctor;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login_only);

        etRegNo = findViewById(R.id.etRegno);
        etCc = findViewById(R.id.etCc);
        etPhone = findViewById(R.id.etPhone_doc);
        etPassword = findViewById(R.id.etPassword);
        loginDoctor = findViewById(R.id.loginDoctor);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        loginDoctor.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void sendOTP(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (context)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        Intent intent = new Intent(DoctorLoginOnly.this, OTPActivity_doctor.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        progressDialog.dismiss();
                        Intent intent = new Intent(DoctorLoginOnly.this, OTPActivity_doctor.class);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }
        );
    }

    private void doctorLogin() {
        final String registration = etRegNo.getText().toString().trim();
        final String cc = etCc.getText().toString().trim();
        final String phone_no = etPhone.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String phone = cc + phone_no;

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                constants.URL_DOCTOR_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.startsWith("Connection successful")) {
                            response = response.replace("Connection successful", "");
                        }

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                DoctorSharedPrefManager.getInstance(getApplicationContext())
                                        .doctorLogin(
                                                obj.getInt("registration_no"),
                                                obj.getString("doctor_name"),
                                                obj.getString("doctor_specialization"),
                                                obj.getString("clinic_address"),
                                                obj.getString("doctor_phone")
                                        );

                                sendOTP(phone);
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "JSON parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("registration_no", registration);
                params.put("doctor_phone", phone);
                params.put("doctor_password", password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == loginDoctor) {
            doctorLogin();
        }
    }
}
