package com.example.capstone_mysql3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.capstone_mysql3.RequestHandler;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, phone, age, password, cc;
    private RadioGroup radioGroup;
    private Button button;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, PatientHomePage.class));
            return;
        }

        name = findViewById(R.id.etName);
        cc = findViewById(R.id.etCc);
        phone = findViewById(R.id.etPhone);
        age = findViewById(R.id.etAge);
        password = findViewById(R.id.etPassword);
        radioGroup = findViewById(R.id.radioGroupGender);
        button = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(this);
    }

    private void registerUser() {
        final String patient_name = name.getText().toString().trim();
        final String patient_cc = cc.getText().toString().trim();
        final String patient_phone1 = phone.getText().toString().trim();
        if (!patient_phone1.matches("\\d{10}")) {
            Toast.makeText(getApplicationContext(), "Phone number should contain 10 digits", Toast.LENGTH_LONG).show();
            return;
        }
        final String patient_phone = patient_cc + patient_phone1;
        final String patient_age = age.getText().toString().trim();
        final String patient_password = password.getText().toString().trim();

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        final String patient_sex;

        if (selectedRadioButtonId == R.id.radioButtonMale) {
            patient_sex = "Male";
        } else if (selectedRadioButtonId == R.id.radioButtonFemale) {
            patient_sex = "Female";
        } else if (selectedRadioButtonId == R.id.radioButtonOthers) {
            patient_sex = "Others (Non Binary, Prefer Not to Say)";
        } else {
            Toast.makeText(getApplicationContext(), "Please select a gender", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(patient_name) || TextUtils.isEmpty(patient_phone) || TextUtils.isEmpty(patient_age) || TextUtils.isEmpty(patient_password)) {
            Toast.makeText(getApplicationContext(), "All fields must be filled out", Toast.LENGTH_LONG).show();
            return;
        }

        if (!patient_age.matches("\\d+")) {
            Toast.makeText(getApplicationContext(), "Age should be a number", Toast.LENGTH_LONG).show();
            return;
        }

        if (patient_password.length() < 8 || !patient_password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$")) {
            Toast.makeText(getApplicationContext(), "Password should be at least 8 characters long and include one letter, one number, and one special character", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("Response", response); // Log the response

                if (response.startsWith("Connection successful")) {
                    response = response.replace("Connection successful", "");
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("error")) {
                        String errorMessage = jsonObject.getString("message");
                        Log.d("Error Message", errorMessage);
                        if ("Please try again with a different phone number".equals(errorMessage)) {
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        initiateOTPVerification(patient_phone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        String errorMessage = error.getMessage();
                        Log.e("Volley Error", errorMessage);

                        if (errorMessage != null && !errorMessage.isEmpty())
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_name", patient_name);
                params.put("patient_phone", patient_phone);
                params.put("patient_sex", patient_sex);
                params.put("patient_age", patient_age);
                params.put("patient_password", patient_password);
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

                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, OTPActivity.class);
                        intent.putExtra("verificationId", ""); // Pass an empty verificationId
                        startActivity(intent);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        String errorMessage = e.getMessage();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(MainActivity.this, "SMS quota exceeded. Please try again later.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Verification failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, OTPActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public void onClick(View v) {
        if (v == button)
            registerUser();
    }
}