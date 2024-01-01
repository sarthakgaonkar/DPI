package com.example.capstone_mysql3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.capstone_mysql3.constants;
import com.example.capstone_mysql3.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editTextPhone, editTextPassword, editTextCc;
    private Button buttonLogin, buttonSignup, buttonDoctorLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextCc = findViewById(R.id.etCc);
        editTextPhone = findViewById(R.id.etPhone);
        editTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.loginPatient);
        buttonSignup = findViewById(R.id.signupPatient);
        buttonDoctorLogin = findViewById(R.id.loginDoctor);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
        buttonDoctorLogin.setOnClickListener(this);
    }

    private void userLogin() {
        final String phone_1 = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String cc_1 = editTextCc.getText().toString().trim();
        if (!phone_1.matches("\\d{10}")) {
            Toast.makeText(getApplicationContext(), "Phone number should contain 10 digits", Toast.LENGTH_LONG).show();
            return;
        }
        final String phone = cc_1+phone_1;

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        // Check if the response contains "Connection successful" and remove it
                        if (response.startsWith("Connection successful")) {
                            response = response.replace("Connection successful", "");
                        }

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("patient_id"),
                                                obj.getString("patient_name"),
                                                obj.getString("patient_phone"),
                                                obj.getString("patient_sex"),
                                                obj.getString("patient_age")
                                        );

                                Toast.makeText(
                                        getApplicationContext(),
                                        "User login successful",
                                        Toast.LENGTH_LONG
                                ).show();

                                initiateOTPVerification(phone);

                            } else {
                                if (obj.has("message")) {
                                    String errorMessage = obj.getString("message");
                                    Toast.makeText(
                                            getApplicationContext(),
                                            errorMessage,
                                            Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Unknown error occurred",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
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
                                "Volley Error: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_phone", phone);
                params.put("patient_password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initiateOTPVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to send OTP to
                60, // Timeout duration
                TimeUnit.SECONDS,
                this, // Activity (context) for callback
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Auto-retrieval of OTP completed (not needed here)
                        // You can proceed with verification here if needed

                        // In your case, you might want to navigate to the OTPActivity immediately
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                        intent.putExtra("verificationId", ""); // Pass an empty verificationId
                        startActivity(intent);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        String errorMessage = e.getMessage();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Toast.makeText(LoginActivity.this, "Invalid phone number", Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            Toast.makeText(LoginActivity.this, "SMS quota exceeded. Please try again later.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Verification failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Code sent to the phone number, you can proceed to OTP verification
                        // Save 'verificationId' and 'forceResendingToken' to be used later in OTPActivity

                        // Start OTPActivity and pass 'verificationId' to it
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        } else if (view == buttonSignup) {
            // Handle signup button click
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (view == buttonDoctorLogin) {
            Intent intent = new Intent(LoginActivity.this, DoctorLoginOnly.class);
            startActivity(intent);
        }
    }
}
