package com.example.capstone_mysql3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class OTPActivity2 extends AppCompatActivity {
    private String verificationId;
    private EditText editTextOTP1, editTextOTP2, editTextOTP3, editTextOTP4, editTextOTP5, editTextOTP6;
    private Button buttonVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity2);

        verificationId = getIntent().getStringExtra("verificationId");

        editTextOTP1 = findViewById(R.id.editText1);
        editTextOTP2 = findViewById(R.id.editText2);
        editTextOTP3 = findViewById(R.id.editText3);
        editTextOTP4 = findViewById(R.id.editText4);
        editTextOTP5 = findViewById(R.id.editText5);
        editTextOTP6 = findViewById(R.id.editText6);

        buttonVerifyOtp = findViewById(R.id.submitButton);
        buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
    }

    private void verifyOTP() {
        String otp = editTextOTP1.getText().toString() + editTextOTP2.getText().toString() +
                editTextOTP3.getText().toString() + editTextOTP4.getText().toString() +
                editTextOTP5.getText().toString() + editTextOTP6.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                             Intent intent = new Intent(OTPActivity2.this, Prescription_Activity.class);
                             startActivity(intent);
                            Toast.makeText(OTPActivity2.this, "Verification successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OTPActivity2.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
