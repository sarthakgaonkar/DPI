package com.example.capstone_mysql3;

public class Medicines {
    private int medicine_id;
    private String medicine_name, medicine_company;

    public Medicines(int medicine_id, String medicine_company, String medicine_name) {
        this.medicine_id = medicine_id;
        this.medicine_company = medicine_company;
        this.medicine_name = medicine_name;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public String getMedicine_company() {
        return medicine_company;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

}

