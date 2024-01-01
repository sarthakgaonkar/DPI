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

public class PatientHomePage extends AppCompatActivity {

    private TextView hellopatientname, patientname, age, sex;
    Button order_hist, calc_bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        hellopatientname = (TextView) findViewById(R.id.tv1);
        patientname = (TextView) findViewById(R.id.tv2);
        age = (TextView) findViewById(R.id.tv3);
        sex = (TextView) findViewById(R.id.tv4);
        order_hist = findViewById(R.id.orderHistory);
        order_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientHomePage.this, OrderHistory.class));
            }
        });

        calc_bmi = findViewById(R.id.calculateBMI);
        calc_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientHomePage.this, BMICalculate.class));
            }
        });

        hellopatientname.setText("Hello " + SharedPrefManager.getInstance(this).getPatientName());
        patientname.setText(SharedPrefManager.getInstance(this).getPatientName());
        age.setText(SharedPrefManager.getInstance(this).getPatientAge());
        sex.setText(SharedPrefManager.getInstance(this).getPatientSex());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Home_Icon) {
                } else if (itemId == R.id.Prescription_Icon) {
                    startActivity(new Intent(PatientHomePage.this, Prescription_Activity_Patient.class));
                } else if (itemId == R.id.Cart_Icon) {
                    startActivity(new Intent(PatientHomePage.this, Cart_Icon_Activity.class));
                } else if (itemId == R.id.Settings_Icon) {
                    startActivity(new Intent(PatientHomePage.this, Settings_Icon_Activity.class));
                }
                return true;
            }
        });

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
                SharedPrefManager.getInstance(this).logout();
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