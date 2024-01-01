package com.example.capstone_mysql3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<Orders> orders;
    private Context context;

    public OrdersAdapter(Context context, List<Orders> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Orders order = orders.get(position);
        holder.orderId.setText("Order ID: " + order.getOrder_id());
        holder.patientName.setText("Patient Name: " + order.getPatient_name());
        holder.medicineName.setText("Medicine Name: " + order.getMedicine_name());
        holder.orderDate.setText("Order Date: " + order.getOrder_date());
        holder.orderAddress.setText("Address: " + order.getShipping_address());
        holder.price.setText("Total: " + order.getTotal());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderId, patientName, medicineName, orderDate, orderAddress, price;

        public ViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            patientName = itemView.findViewById(R.id.patientName);
            medicineName = itemView.findViewById(R.id.medicineName);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            price = itemView.findViewById(R.id.price);
        }
    }
}