package com.example.capstone_mysql3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Doctor_HomePage extends AppCompatActivity {
    Button prescription_view, add_pres, calc_bmi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_home_page);
        TextView docNameTextView = findViewById(R.id.docName);
        TextView docSpecializationTextView = findViewById(R.id.docSpecialization);
        TextView docPhoneTextView = findViewById(R.id.docPhone);
        TextView docRegNoTextView = findViewById(R.id.docRegNo);
        String doctorName = DoctorSharedPrefManager.getInstance(this).getDoctorName();
        String doctorSpecialization = DoctorSharedPrefManager.getInstance(this).getDoctorSpecialization();
        String doctorPhone = DoctorSharedPrefManager.getInstance(this).getPhone();
        int registrationNo = DoctorSharedPrefManager.getInstance(this).getRegistrationNo();

        docNameTextView.setText(doctorName);
        docSpecializationTextView.setText(doctorSpecialization);
        docPhoneTextView.setText(doctorPhone);
        docRegNoTextView.setText(String.valueOf(registrationNo));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Home_Icon) {
                } else if (itemId == R.id.Prescription_Icon) {
                    startActivity(new Intent(Doctor_HomePage.this, DoctorFetchPrescription.class));
                } else if (itemId == R.id.Cart_Icon) {
                    startActivity(new Intent(Doctor_HomePage.this, Cart_Icon_Activity.class));
                } else if (itemId == R.id.Settings_Icon) {
                    startActivity(new Intent(Doctor_HomePage.this, Settings_Icon_Activity.class));
                }
                return true;
            }
        });
        prescription_view = findViewById(R.id.patient_pres_list);
        prescription_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Doctor_HomePage.this, DoctorFetchPrescription.class));
            }
        });

        calc_bmi = findViewById(R.id.calculateBMI);
        calc_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Doctor_HomePage.this, BMICalculate.class));
            }
        });

        add_pres = findViewById(R.id.addPrescription);
        add_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Doctor_HomePage.this,Prescribe_medicines_1.class));
            }
        });


//        pres_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Doctor_HomePage.this,DoctorFetchPrescription.class));
//            }
//        });


        if (!DoctorSharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        TextView texthello = (TextView) findViewById(R.id.tv1);


        texthello.setText("Hello " + DoctorSharedPrefManager.getInstance(this).getDoctorName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                DoctorSharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}