package com.example.capstone_mysql3;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionPatientAdapter extends RecyclerView.Adapter<PrescriptionPatientAdapter.PrescriptionViewHolder>{

    private Context mCtx;
    private List<Prescriptions> PrescriptionsList;
    public PrescriptionPatientAdapter(Context mCtx, List<Prescriptions> PrescriptionsList) {
        this.mCtx = mCtx;
        this.PrescriptionsList = PrescriptionsList;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listlayout_patientprescriptions, null);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {

        final Prescriptions Prescriptions = PrescriptionsList.get(position);
        if (Prescriptions != null) {
            holder.textViewPrescription_ID.setText(String.valueOf(Prescriptions.getPrescription_id()));

            if (Prescriptions.getRegistration_no() != 0) {
                holder.textViewRegNo.setText(String.valueOf(Prescriptions.getRegistration_no()));
            }

            if (Prescriptions.getTitle() != null) {
                holder.title.setText(Prescriptions.getTitle());
            }

            if (Prescriptions.getStart_date() != null) {
                holder.textViewStart_date.setText(Prescriptions.getStart_date());
            }

            if (Prescriptions.getEnd_date() != null) {
                holder.textViewEnd_date.setText(String.valueOf(Prescriptions.getEnd_date()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date endDate = sdf.parse(Prescriptions.getEnd_date());
                    Date currentDate = new Date();
                    if (currentDate.after(endDate)) {
                        holder.btnBuy.setEnabled(false);
                        holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.grey));
                    } else {
                        holder.btnBuy.setEnabled(true);
                        holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.light_blue));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    holder.btnBuy.setEnabled(false);
                    holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.grey));
                }
            }

            if (Prescriptions.getDoctor_name() != null) {
                holder.textViewDoctor_name.setText(String.valueOf(Prescriptions.getDoctor_name()));
            }
            if (Prescriptions.getMedicine_name() != null) {
                holder.textViewMedicine_name.setText(String.valueOf(Prescriptions.getMedicine_name()));
            }
        }


        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prescriptionId = Prescriptions.getPrescription_id();
                fetchAndLaunchOrderActivity(prescriptionId);
            }
        });


        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = Prescription_Activity_Patient.getBitmapFromView(holder.itemView);
                    File directory = mCtx.getExternalFilesDir(null);
                    String fileName = "Prescription_" + Prescriptions.getPrescription_id() + ".pdf";

                    File pdfFile = Prescription_Activity_Patient.convertBitmapToPdfWithText(bitmap, directory, fileName);
                    Prescription_Activity_Patient.sharePDF(pdfFile, mCtx, (Activity) mCtx);
                } catch (IOException e) {
                    Toast.makeText(mCtx, "Error sharing the prescription", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchAndLaunchOrderActivity(int prescriptionId) {
        String url = constants.URL_GET_MEDICINES_BY_PRESCRIPTIONS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // TODO: handle the server's response here
                    Intent intent = new Intent(mCtx, OrderPage_01.class);
                    intent.putExtra("medicineData", response);
                    mCtx.startActivity(intent);
                },
                error -> {

                    Toast.makeText(mCtx, "Failed to fetch data. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("VolleyRequestError", "Error: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("prescription_id", String.valueOf(prescriptionId));
                return params;
            }
        };
        Volley.newRequestQueue(mCtx).add(stringRequest);
    }



    @Override
    public int getItemCount() {
        return PrescriptionsList.size();
    }

    class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPrescription_ID, textViewRegNo, textViewStart_date, textViewEnd_date, textViewDoctor_name, textViewMedicine_name, title;
        Button btnShare, btnBuy;
        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPrescription_ID = itemView.findViewById(R.id.textViewPrescription_ID);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            textViewStart_date = itemView.findViewById(R.id.textViewStart_date);
            textViewEnd_date = itemView.findViewById(R.id.textViewEnd_date);
            textViewDoctor_name = itemView.findViewById(R.id.textViewDoctor_name);
            textViewMedicine_name = itemView.findViewById(R.id.textViewMedicine_name);
            title = itemView.findViewById(R.id.title);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btnShare = itemView.findViewById(R.id.share);
        }
    }
}