package com.example.capstone_mysql3;

import java.io.Serializable;

public class GenericMedicine implements Serializable{
    private int medicineId;
    private String medicineName;
    private float medicine_price;
    private int quantity = 1;

    public GenericMedicine(int medicineId, String medicineName, float medicinePrice, int quantity) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicine_price = medicinePrice;
        this.quantity = quantity;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public float getMedicinePrice() {
        return medicine_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}