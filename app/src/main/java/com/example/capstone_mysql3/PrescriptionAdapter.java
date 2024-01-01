//package com.example.capstone_mysql3;

//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.itextpdf.io.source.ByteArrayOutputStream;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>{
//
//    //this context we will use to inflate the layout
//    private Context mCtx;
//
//    //we are storing all the Prescriptions in a list
//    private List<Prescriptions> PrescriptionsList;
//
//    //getting the context and Prescription list with constructor
//    public PrescriptionAdapter(Context mCtx, List<Prescriptions> PrescriptionsList) {
//        this.mCtx = mCtx;
//        this.PrescriptionsList = PrescriptionsList;
//    }
//
//    @NonNull
//    @Override
//    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        //inflating and returning our view holder
//        LayoutInflater inflater = LayoutInflater.from(mCtx);
//        View view = inflater.inflate(R.layout.listlayoutprescriptions, null);
//        return new PrescriptionViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
//
//        //getting the Prescription of the specified position
//        Prescriptions Prescriptions = PrescriptionsList.get(position);
//
//        //binding the data with the viewholder views
//        if (Prescriptions != null) {
//            holder.textViewPrescription_ID.setText(String.valueOf(Prescriptions.getPrescription_id()));
//
//            if (Prescriptions.getRegistration_no() != -1) {
//                holder.textViewRegNo.setText(String.valueOf(Prescriptions.getRegistration_no()));
//            }
//
//            if (Prescriptions.getTitle() != null) {
//                holder.textViewStart_date.setText(Prescriptions.getTitle());
//            }
//
//
//            if (Prescriptions.getStart_date() != null) {
//                holder.textViewStart_date.setText(Prescriptions.getStart_date());
//            }
//
//            if (Prescriptions.getEnd_date() != null) {
//                holder.textViewEnd_date.setText(String.valueOf(Prescriptions.getEnd_date()));
//            }
//            if (Prescriptions.getDoctor_name() != null) {
//                holder.textViewDoctor_name.setText(String.valueOf(Prescriptions.getDoctor_name()));
//            }
//            if (Prescriptions.getMedicine_name() != null) {
//                holder.textViewMedicine_name.setText(String.valueOf(Prescriptions.getMedicine_name()));
//            }
//        }
//
//
//        holder.btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    // Convert the current prescription view to a bitmap
//                    Bitmap bitmap = Prescription_Activity_Patient.getBitmapFromView(holder.itemView);
//
//                    // Define a directory and filename for the PDF
//                    File directory = mCtx.getExternalFilesDir(null);
//                    String fileName = "Prescription_" + Prescriptions.getPrescription_id() + ".pdf";
//
//                    // Convert the bitmap to a PDF with text
//                    File pdfFile = Prescription_Activity_Patient.convertBitmapToPdfWithText(bitmap, directory, fileName);
//
//                    // Share the PDF
//                    Prescription_Activity_Patient.sharePDF(pdfFile, mCtx, (Activity) mCtx);
//                } catch (IOException e) {
//                    Toast.makeText(mCtx, "Error sharing the prescription", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
////        holder.btnShare.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Bitmap bitmap = Prescription_Activity_Patient.getBitmapFromView(holder.itemView);
////
////                // Compress the bitmap
////                ByteArrayOutputStream out = new ByteArrayOutputStream();
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
////                Bitmap compressedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
////
////                try {
////                    // Convert the compressed bitmap view to a bitmap
////                    File directory = mCtx.getExternalFilesDir(null);
////                    String fileName = "Prescription_" + Prescriptions.getPrescription_id() + ".pdf";
////
////                    // Convert the compressed bitmap to a PDF with text
////                    File pdfFile = Prescription_Activity_Patient.convertBitmapToPdfWithText(compressedBitmap, directory, fileName);
////
////                    // Share the PDF
////                    Prescription_Activity_Patient.sharePDF(pdfFile, mCtx, (Activity) mCtx);
////                } catch (IOException e) {
////                    Toast.makeText(mCtx, "Error sharing the prescription", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
////        holder.btnShare.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Bitmap originalBitmap = Prescription_Activity_Patient.getBitmapFromView(holder.itemView);
////
////                // Calculate new scaled dimensions, e.g., reduce by 50%
////                int width = originalBitmap.getWidth() / 2;
////                int height = originalBitmap.getHeight() / 2;
////
////                // Scale down the bitmap
////                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
////
////                try {
////                    // Convert the scaled bitmap view to a bitmap
////                    File directory = mCtx.getExternalFilesDir(null);
////                    String fileName = "Prescription_" + Prescriptions.getPrescription_id() + ".pdf";
////
////                    // Convert the scaled bitmap to a PDF with text
////                    File pdfFile = Prescription_Activity_Patient.convertBitmapToPdfWithText(scaledBitmap, directory, fileName);
////
////                    // Share the PDF
////                    Prescription_Activity_Patient.sharePDF(pdfFile, mCtx, (Activity) mCtx);
////                } catch (IOException e) {
////                    Toast.makeText(mCtx, "Error sharing the prescription", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//
//
//        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: Define the action when the 'Buy' button is clicked.
//                Toast.makeText(mCtx, "Buy functionality is not yet implemented!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return PrescriptionsList.size();
//    }
//
//    class PrescriptionViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewPrescription_ID, textViewRegNo, textViewStart_date, textViewEnd_date, textViewDoctor_name, textViewMedicine_name, textViewTitle;
//        Button btnBuy;
//        ImageButton btnShare;
//        public PrescriptionViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            textViewPrescription_ID = itemView.findViewById(R.id.textViewRating);
//            textViewRegNo = itemView.findViewById(R.id.textViewRating2);
//            textViewStart_date = itemView.findViewById(R.id.textViewShortDesc);
//            textViewEnd_date = itemView.findViewById(R.id.textViewShortDesc2);
//            textViewDoctor_name = itemView.findViewById(R.id.textViewTitle);
//            textViewMedicine_name = itemView.findViewById(R.id.textViewextra);
//            btnBuy = itemView.findViewById(R.id.btnBuy);
//            btnShare = itemView.findViewById(R.id.shareButton);
//            textViewTitle = itemView.findViewById(R.id.tvTitle1);
//        }
//    }
//}

//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>{
//
//    //this context we will use to inflate the layout
//    private Context mCtx;
//
//    //we are storing all the Prescriptions in a list
//    private List<Prescriptions> PrescriptionsList;
//
//    //getting the context and Prescription list with constructor
//    public PrescriptionAdapter(Context mCtx, List<Prescriptions> PrescriptionsList) {
//        this.mCtx = mCtx;
//        this.PrescriptionsList = PrescriptionsList;
//    }
//
//    @NonNull
//    @Override
//    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        //inflating and returning our view holder
//        LayoutInflater inflater = LayoutInflater.from(mCtx);
//        View view = inflater.inflate(R.layout.listlayoutprescriptions, null);
//        return new PrescriptionViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
//
//        //getting the Prescription of the specified position
//        Prescriptions Prescriptions = PrescriptionsList.get(position);
//
//        //binding the data with the viewholder views
//        if (Prescriptions != null) {
//            holder.textViewPrescription_ID.setText(String.valueOf(Prescriptions.getPrescription_id()));
//
//
//            if (Prescriptions.getStart_date() != null) {
//                holder.textViewStart_date.setText(Prescriptions.getStart_date());
//            }
//
//            if (Prescriptions.getEnd_date() != null) {
//                holder.textViewEnd_date.setText(String.valueOf(Prescriptions.getEnd_date()));
//            }
//            if (Prescriptions.getDoctor_name() != null) {
//                holder.textViewDoctor_name.setText(String.valueOf(Prescriptions.getDoctor_name()));
//            }
//            if (Prescriptions.getMedicine_name() != null) {
//                holder.textViewMedicine_name.setText(String.valueOf(Prescriptions.getMedicine_name()));
//            }
//        }
//
//        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    // Convert the current prescription view to a bitmap
//                    Bitmap bitmap = Prescription_Activity.getBitmapFromView(holder.itemView);
//
//                    // Define a directory and filename for the PDF
//                    File directory = mCtx.getExternalFilesDir(null);
//                    String fileName = "Prescription_" + Prescriptions.getPrescription_id() + ".pdf";
//
//                    // Convert the bitmap to a PDF with text
//                    File pdfFile = Prescription_Activity.convertBitmapToPdfWithText(bitmap, directory, fileName);
//
//                    // Share the PDF
//                    Prescription_Activity.sharePDF(pdfFile, mCtx, (Activity) mCtx);
//                } catch (IOException e) {
//                    Toast.makeText(mCtx, "Error sharing the prescription", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return PrescriptionsList.size();
//    }
//
//    class PrescriptionViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewPrescription_ID, textViewStart_date, textViewEnd_date, textViewDoctor_name, textViewMedicine_name;
//        Button buttonShare;
//        public PrescriptionViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            textViewPrescription_ID = itemView.findViewById(R.id.textViewTitle);
//            textViewStart_date = itemView.findViewById(R.id.textViewShortDesc);
//            textViewEnd_date = itemView.findViewById(R.id.textViewPrice);
//            textViewDoctor_name = itemView.findViewById(R.id.textViewRating);
//            textViewMedicine_name = itemView.findViewById(R.id.textViewextra);
//            buttonShare = itemView.findViewById(R.id.share);
//        }
//    }
//}



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

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>{

    private Context mCtx;

    private List<Prescriptions> PrescriptionsList;

    public PrescriptionAdapter(Context mCtx, List<Prescriptions> PrescriptionsList) {
        this.mCtx = mCtx;
        this.PrescriptionsList = PrescriptionsList;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listlayoutprescriptions, null);
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

//            if (Prescriptions.getEnd_date() != null) {
//                holder.textViewEnd_date.setText(String.valueOf(Prescriptions.getEnd_date()));
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                    Date endDate = sdf.parse(Prescriptions.getEnd_date());
//                    Date currentDate = new Date();
//
//                    if (currentDate.after(endDate)) {
//                        holder.btnBuy.setEnabled(false);
//                        holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.grey));
//                    } else {
//                        holder.btnBuy.setEnabled(true);
//                        holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.light_blue));
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    holder.btnBuy.setEnabled(false);
//                    holder.btnBuy.setBackgroundColor(mCtx.getResources().getColor(R.color.grey));
//                }
//            }

            if (Prescriptions.getDoctor_name() != null) {
                holder.textViewDoctor_name.setText(String.valueOf(Prescriptions.getDoctor_name()));
            }
            if (Prescriptions.getMedicine_name() != null) {
                holder.textViewMedicine_name.setText(String.valueOf(Prescriptions.getMedicine_name()));
            }
        }


//        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int prescriptionId = Prescriptions.getPrescription_id();
//                fetchAndLaunchOrderActivity(prescriptionId);
//            }
//        });


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
        Button btnShare;
        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPrescription_ID = itemView.findViewById(R.id.textViewPrescription_ID);
            textViewRegNo = itemView.findViewById(R.id.textViewRegNo);
            textViewStart_date = itemView.findViewById(R.id.textViewStart_date);
            textViewEnd_date = itemView.findViewById(R.id.textViewEnd_date);
            textViewDoctor_name = itemView.findViewById(R.id.textViewDoctor_name);
            textViewMedicine_name = itemView.findViewById(R.id.textViewMedicine_name);
            title = itemView.findViewById(R.id.title);
            btnShare = itemView.findViewById(R.id.share);
        }
    }
}