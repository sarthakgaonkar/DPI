package com.example.capstone_mysql3;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_PATIENTNAME = "patient_name";
    private static final String KEY_PATIENT_PHONE = "patient_phone";
    private static final String KEY_PATIENT_SEX = "patient_sex";
    private static final String KEY_PATIENT_AGE = "patient_age";
    private static final String KEY_PATIENT_ID = "patient_id";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int patient_id, String patient_name, String patient_phone, String patient_sex, String patient_age) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_PATIENT_ID, patient_id);
        editor.putString(KEY_PATIENTNAME, patient_name);
        editor.putString(KEY_PATIENT_PHONE, patient_phone);
        editor.putString(KEY_PATIENT_SEX, patient_sex);
        editor.putString(KEY_PATIENT_AGE, patient_age);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_PATIENTNAME, null) != null) {
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

    public String getPatientName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PATIENTNAME, null);
    }

    public String getPatientAge(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PATIENT_AGE, null);
    }

    public String getPatientSex(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PATIENT_SEX, null);
    }

    public int getPatientId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_PATIENT_ID, -1);
    }
}