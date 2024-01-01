package com.example.capstone_mysql3;

import android.content.Context;
import android.content.SharedPreferences;

public class DoctorSharedPrefManager {


    private static DoctorSharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_REGISTRATIONNO = "registration_no";
    private static final String KEY_DOCTOR_NAME = "doctor_name";
    private static final String KEY_DOCTORSPECIALIZATION = "doctor_specialization";
    private static final String KEY_CLINICADDRESS = "clinic_address";
    private static final String KEY_DOCTORPHONE = "doctor_phone";


    private DoctorSharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized DoctorSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DoctorSharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean doctorLogin(int registration_no, String doctor_name, String doctor_specialization, String clinic_address, String doctor_phone) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_REGISTRATIONNO, registration_no);

//        editor.putString(KEY_REGISTRATIONNO, registration_no);
        editor.putString(KEY_DOCTOR_NAME, doctor_name);
        editor.putString(KEY_DOCTORSPECIALIZATION, doctor_specialization);
        editor.putString(KEY_CLINICADDRESS, clinic_address);
        editor.putString(KEY_DOCTORPHONE, doctor_phone);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_DOCTOR_NAME, null) != null) {
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getDoctorName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DOCTOR_NAME, null);
    }

    public String getDoctorSpecialization(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DOCTORSPECIALIZATION, null);
    }

    public String getClinicAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CLINICADDRESS, null);
    }

    public String getPhone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DOCTORPHONE, null);
    }

    public int getRegistrationNo(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_REGISTRATIONNO, -1);
    }
}
