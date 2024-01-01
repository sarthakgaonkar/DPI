package com.example.capstone_mysql3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Prescription_Activity extends AppCompatActivity {


    List<Prescriptions> PrescriptionsList;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PrescriptionsList = new ArrayList<>();
        PrescriptionAdapter adapter = new PrescriptionAdapter(this, PrescriptionsList);
        recyclerView.setAdapter(adapter);

        loadPrescriptions();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Prescription_Icon);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Home_Icon) {
                    startActivity(new Intent(Prescription_Activity.this, Doctor_HomePage.class));
                } else if (itemId == R.id.Prescription_Icon) {
                } else if (itemId == R.id.Cart_Icon) {
                    startActivity(new Intent(Prescription_Activity.this, Cart_Icon_Activity.class));
                } else if (itemId == R.id.Settings_Icon) {
                    startActivity(new Intent(Prescription_Activity.this, Settings_Icon_Activity.class));
                }
                return true;
            }
        });

    }

    private void loadPrescriptions() {

        int patientId = -1;
        String urlWithParameters;


//        if (getIntent().hasExtra("PATIENT_ID")) {
            patientId = getIntent().getIntExtra("PATIENT_ID", -1);
            Log.d("PrescriptionActivity", "Received patientId: " + patientId);
//        }

//        else{
//            patientId = SharedPrefManager.getInstance(this).getPatientId();
//        }
        urlWithParameters = constants.URL_GET_PRESCRIPTIONS + "?patient_id=" + patientId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlWithParameters, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    if (response != null && !response.isEmpty()) {
                        JSONArray prescriptions = new JSONArray(response);

                        if (prescriptions.length() > 0) {
                            for (int i = 0; i < prescriptions.length(); i++) {
                                JSONObject prescriptionObject = prescriptions.getJSONObject(i);
                                int registration_no = prescriptionObject.getInt("registration_no");
                                int prescription_id = prescriptionObject.getInt("prescription_id");
                                String start_date = prescriptionObject.getString("start_date");
                                String end_date = prescriptionObject.getString("end_date");
                                String doctor_name = prescriptionObject.getString("doctor_name");
                                String medicine_name = prescriptionObject.getString("medicine_name");
                                String title = prescriptionObject.getString("title");

                                Prescriptions Prescriptions = new Prescriptions(registration_no,prescription_id, start_date, end_date, doctor_name, medicine_name, title);
                                PrescriptionsList.add(Prescriptions);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            Toast.makeText(Prescription_Activity.this, "No prescriptions found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Prescription_Activity.this, "Empty or invalid response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON Parsing Error", e.getMessage());
                    Toast.makeText(Prescription_Activity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getMessage());
                Toast.makeText(Prescription_Activity.this, "An error occurred", Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    static File convertBitmapToPdfWithText(Bitmap bitmap, File dir, String fileName) throws IOException {
        File file = new File(dir, fileName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc, PageSize.A4);

        Image img = new Image(com.itextpdf.io.image.ImageDataFactory.create(stream.toByteArray()));
        doc.add(img);
        doc.add(new Paragraph("This PDF was generated by Digital Prescription Interface and does not require a signature")
                .setFixedPosition(10, 20, PageSize.A4.getWidth() - 20));

        doc.close();
        return file;
    }

    static void sharePDF(File file, Context mCtx, Activity activity) {
        Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setDataAndType(contentUri, "application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        if (shareIntent.resolveActivity(mCtx.getPackageManager()) != null) {
            activity.startActivity(shareIntent);
        } else {
            Toast.makeText(mCtx, "No app available to share the prescription", Toast.LENGTH_SHORT).show();
        }

    }

}