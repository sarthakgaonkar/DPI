package com.example.capstone_mysql3;

public class Orders {
    private int order_id;
    private String patient_name;
    private String order_date;
    private String shipping_address;
    private String medicine_name;
    private int total;


    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public int getTotal() {
        return total;
    }
}