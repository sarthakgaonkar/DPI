package com.example.capstone_mysql3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMICalculate extends AppCompatActivity {

    private EditText weightInput;
    private EditText heightInput;
    private TextView bmiResult;
    private TextView bmiStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculate);

        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        bmiResult = findViewById(R.id.bmiResult);
        bmiStatus = findViewById(R.id.bmiStatus);

        Button calculateButton = findViewById(R.id.calculateButton);
        Button resetButton = findViewById(R.id.resetButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);
            float bmi = weight / (height * height);
            String bmi1 = String.valueOf(bmi);
            bmiResult.setText("Your BMI is: " + bmi1);

            if (bmi < 18.5) {
                bmiStatus.setText("You are Underweight");
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                bmiStatus.setText("Normal");
            } else if (bmi >= 25 && bmi <= 29.9) {
                bmiStatus.setText("Overweight");
            } else if (bmi >= 30 && bmi <= 34.9) {
                bmiStatus.setText("Mild Obesity");
            } else if (bmi >= 35 && bmi <= 39.9) {
                bmiStatus.setText("Moderate Obesity");
            } else {
                bmiStatus.setText("Morbid Obesity");
            }
        }
    }

    private void resetFields() {
        weightInput.setText("");
        heightInput.setText("");
        bmiResult.setText("Your BMI");
        bmiStatus.setText("Your BMI Result");
    }
}