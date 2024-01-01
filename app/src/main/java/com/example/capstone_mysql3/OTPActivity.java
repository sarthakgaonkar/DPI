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
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.example.capstone_mysql3.SharedPrefManager;
//
//public class OTPActivity extends AppCompatActivity {
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
//                    Toast.makeText(OTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(OTPActivity.this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
////                            Intent intent = new Intent(OTPActivity.this, PatientHomePage.class);
//
//                            startActivity(new Intent(OTPActivity.this, PatientHomePage.class));
//                            finish();
//
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                Toast.makeText(OTPActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(OTPActivity.this, "Authentication failed. Please try again later.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//}
//
//

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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.capstone_mysql3.SharedPrefManager;

public class OTPActivity extends AppCompatActivity {

    private String phoneNumber;
    private EditText[] otpEditTexts;
    private Button verifyButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private boolean isDoctorLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        isDoctorLogin = getIntent().getBooleanExtra("isDoctorLogin", false);

        otpEditTexts = new EditText[] {
                findViewById(R.id.editText1),
                findViewById(R.id.editText2),
                findViewById(R.id.editText3),
                findViewById(R.id.editText4),
                findViewById(R.id.editText5),
                findViewById(R.id.editText6)
        };
        verifyButton = findViewById(R.id.submitButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        final String verificationId = getIntent().getStringExtra("verificationId");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = getEnteredOTP();

                if (!otp.isEmpty()) {
                    progressDialog.setMessage("Verifying OTP...");
                    progressDialog.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(OTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getEnteredOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText editText : otpEditTexts) {
            otpBuilder.append(editText.getText().toString().trim());
        }
        return otpBuilder.toString();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(OTPActivity.this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
                            if (isDoctorLogin) {
                                startActivity(new Intent(OTPActivity.this, Doctor_HomePage.class));
                            } else {
                                startActivity(new Intent(OTPActivity.this, PatientHomePage.class));
                            }
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OTPActivity.this, "Authentication failed. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
