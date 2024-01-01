//package com.example.capstone_mysql3;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.example.capstone_mysql3.SharedPrefManager;
//
//public class OTPActivity_doctor extends AppCompatActivity {
//
//    private String phoneNumber;
//    private EditText[] otpEditTexts;
//    private Button verifyButton;
//    private ProgressDialog progressDialog;
//    private FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otpactivity);
//
//        phoneNumber = getIntent().getStringExtra("phoneNumber");
//
//        otpEditTexts = new EditText[] {
//                findViewById(R.id.editText1),
//                findViewById(R.id.editText2),
//                findViewById(R.id.editText3),
//                findViewById(R.id.editText4),
//                findViewById(R.id.editText5),
//                findViewById(R.id.editText6)
//        };
//        verifyButton = findViewById(R.id.submitButton);
//        progressDialog = new ProgressDialog(this);
//        firebaseAuth = FirebaseAuth.getInstance();
//        final String verificationId = getIntent().getStringExtra("verificationId");
//
//        verifyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String otp = getEnteredOTP();
//
//                if (!otp.isEmpty()) {
//                    progressDialog.setMessage("Verifying OTP...");
//                    progressDialog.show();
//
//                    // Verify the entered OTP
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
//                    signInWithPhoneAuthCredential(credential);
//                } else {
//                    Toast.makeText(OTPActivity_doctor.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private String getEnteredOTP() {
//        StringBuilder otpBuilder = new StringBuilder();
//        for (EditText editText : otpEditTexts) {
//            otpBuilder.append(editText.getText().toString().trim());
//        }
//        return otpBuilder.toString();
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressDialog.dismiss();
//
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = task.getResult().getUser();
//                            Toast.makeText(OTPActivity_doctor.this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
//
//                            // In the DoctorLoginOnly activity, you may have stored some doctor login info.
//                            // You can retrieve this info and use it in your next activity.
//                            // For example, use SharedPrefManager to store and retrieve doctor information.
//
//                            // Example usage:
//                            // String doctorName = SharedPrefManager.getInstance(OTPActivity_doctor.this).getDoctorName();
//
//                            startActivity(new Intent(OTPActivity_doctor.this, Doctor_HomePage.class));
//                            finish();
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                Toast.makeText(OTPActivity_doctor.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(OTPActivity_doctor.this, "Authentication failed. Please try again later.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//}

package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity_doctor extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private Button submitButton;
    private String verificationId;
    private String phone;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpdoctor);

        verificationId = getIntent().getStringExtra("verificationId");
        phone = getIntent().getStringExtra("phone");

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void verifyOTP() {
        String otp = editText1.getText().toString() +
                editText2.getText().toString() +
                editText3.getText().toString() +
                editText4.getText().toString() +
                editText5.getText().toString() +
                editText6.getText().toString();

        if (otp.length() == 6) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(OTPActivity_doctor.this, Doctor_HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

