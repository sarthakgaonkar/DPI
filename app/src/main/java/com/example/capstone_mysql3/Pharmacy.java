package com.example.capstone_mysql3;

public class Pharmacy {
    private String name;
    private String address;
    private String phoneNumber;
    private String timings;
    private double distance;

    public Pharmacy(String name, String address, String phoneNumber, String timings, double distance) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.timings = timings;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTimings() {
        return timings;
    }

    public double getDistance() {
        return distance;
    }
}

