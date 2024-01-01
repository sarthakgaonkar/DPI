
package com.example.capstone_mysql3;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class DoctorFetchPrescription extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private Button buttonCheckPhoneNumber, submitButton;
    private EditText[] otpBoxes = new EditText[6];
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_fetch_prescription);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonCheckPhoneNumber = findViewById(R.id.buttonCheckPhoneNumber);
        submitButton = findViewById(R.id.submitButton);

        otpBoxes[0] = findViewById(R.id.editText1);
        otpBoxes[1] = findViewById(R.id.editText2);
        otpBoxes[2] = findViewById(R.id.editText3);
        otpBoxes[3] = findViewById(R.id.editText4);
        otpBoxes[4] = findViewById(R.id.editText5);
        otpBoxes[5] = findViewById(R.id.editText6);

        for (EditText box : otpBoxes) {
            box.setEnabled(false);
        }
        submitButton.setEnabled(false);


        mAuth = FirebaseAuth.getInstance();

        buttonCheckPhoneNumber.setOnClickListener(v -> checkPhoneNumber());
        submitButton.setOnClickListener(v -> signInWithOtp());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Prescription_Icon);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Home_Icon) {
                    startActivity(new Intent(DoctorFetchPrescription.this, Doctor_HomePage.class));
                } else if (itemId == R.id.Prescription_Icon) {
                } else if (itemId == R.id.Cart_Icon) {
                    startActivity(new Intent(DoctorFetchPrescription.this, Cart_Icon_Activity.class));
                } else if (itemId == R.id.Settings_Icon) {
                    startActivity(new Intent(DoctorFetchPrescription.this, Settings_Icon_Activity.class));
                }
                return true;
            }
        });

    }

    private void checkPhoneNumber() {
        StringRequest request = new StringRequest(Request.Method.POST, constants.URL_Validate_USER, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    patientId = jsonObject.getInt("patient_id");
                    sendOtp();
                } else {
                    Toast.makeText(DoctorFetchPrescription.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(DoctorFetchPrescription.this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("patient_phone", editTextPhoneNumber.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void sendOtp() {
        String phoneNumber = editTextPhoneNumber.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(DoctorFetchPrescription.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        mVerificationId = verificationId;
                        Toast.makeText(DoctorFetchPrescription.this, "Code Sent", Toast.LENGTH_SHORT).show();
                        for (EditText box : otpBoxes) {
                            box.setEnabled(true);
                        }
                        submitButton.setEnabled(true);
                    }
                });
    }

    private void signInWithOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText box : otpBoxes) {
            otp.append(box.getText().toString());
        }

        if (otp.length() < otpBoxes.length) {  // Check if the OTP length is less than expected
            Toast.makeText(DoctorFetchPrescription.this, "Please fill all OTP fields.", Toast.LENGTH_SHORT).show();
            return; // Return without proceeding further
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                redirectToPrescriptionActivity();
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(DoctorFetchPrescription.this, "Invalid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToPrescriptionActivity() {
        Intent intent = new Intent(DoctorFetchPrescription.this, Prescription_Activity.class);
        intent.putExtra("PATIENT_ID", patientId);
        startActivity(intent);
    }
}
