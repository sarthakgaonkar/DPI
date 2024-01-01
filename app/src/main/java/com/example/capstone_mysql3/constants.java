package com.example.capstone_mysql3;

public class constants {

    private static final String ROOT_URL = "http://192.168.1.55/Android/includes/";

    public static final String URL_REGISTER = ROOT_URL+"registerUser.php";
    public static final String URL_LOGIN = ROOT_URL+"userLogin.php";
    public static final String URL_DOCTOR_LOGIN = ROOT_URL+"doctorLogin.php";
    public static final String URL_GET_MEDICINES = ROOT_URL+"getMedicines.php";
    public static final String URL_GET_PRESCRIPTIONS = ROOT_URL+"getPrescriptions.php";
    public static final String URL_Validate_USER = ROOT_URL+"validateUser.php";
    public static final String URL_INSERT_PRESCRIPTIONS = ROOT_URL+"insertPrescription.php";
    public static final String URL_GET_MEDICINES_BY_PRESCRIPTIONS = ROOT_URL+"getMedicinesByPrescription.php";
    public static final String NON_PRESCRIPTION_MEDS = ROOT_URL+"nonPrescriptionMeds.php";

    public static final String INSERT_ORDERS = ROOT_URL+"insertOrder.php";
    public static final String FETCH_ORDERS = ROOT_URL+"fetchAllOrdersByPrescriptionId.php";

}
