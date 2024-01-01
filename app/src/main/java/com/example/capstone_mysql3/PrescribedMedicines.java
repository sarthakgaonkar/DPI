package com.example.capstone_mysql3;

public class PrescribedMedicines {
    private int medicineId;
    private String medicineName;
    private int quantity = 1;
    float medicine_price;

    public PrescribedMedicines(int medicineId, String medicineName, float medicine_price) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicine_price=medicine_price;
    }

    public float getMedicine_price() {
        return medicine_price;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
