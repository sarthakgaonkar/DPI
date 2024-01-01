package com.example.capstone_mysql3;

public class Prescriptions {
    private  int prescription_id;
    private int registration_no;
    private String start_date, end_date, doctor_name, medicine_name, title;

    public Prescriptions(int registration_no, int prescription_id, String start_date, String end_date,String doctor_name, String medicine_name, String title) {
        this.registration_no = registration_no;
        this.prescription_id = prescription_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.doctor_name = doctor_name;
        this.medicine_name = medicine_name;
        this.title = title;
    }

    public int getRegistration_no()
    {
        return registration_no;
    }

    public  int getPrescription_id() {
        return prescription_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getTitle()
    {
        return title;
    }
}