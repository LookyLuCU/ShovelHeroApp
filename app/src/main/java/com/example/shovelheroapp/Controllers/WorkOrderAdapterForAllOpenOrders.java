package com.example.shovelheroapp.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;

import java.util.List;

public class WorkOrderAdapterForAllOpenOrders extends RecyclerView.Adapter<WorkOrderAdapterForAllOpenOrders.ViewHolder> {
    private List<WorkOrder> workOrders;
    private Context context;

    public WorkOrderAdapterForAllOpenOrders(Context context, List<WorkOrder> workOrders) {
        this.context = context;
        this.workOrders = workOrders;
    }

    //ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView addressImage;
        public TextView requestDateTV;
        //public TextView distanceTV;
        public TextView sqFootageTV;
        public TextView statusTV;

        public ViewHolder(View view) {
            super(view);
            //addressImage = view.findViewById(R.id.imgPropertyImage);
            requestDateTV = view.findViewById(R.id.tvRequestDate);
            //distanceTV = view.findViewById(R.id.tvDistance);
            sqFootageTV = view.findViewById(R.id.tvSquareFootage);
            statusTV = view.findViewById(R.id.tvStatus);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.work_order_item_open_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkOrder workOrder = workOrders.get(position);

        Log.d("WorkOrderAdapter", "onBindViewHolder: " + workOrder.getStatus());

        //TODO: get initial property address (from Address)
        //holder.addressImage.setImageBitmap(workOrder.getAddressImage);
        holder.requestDateTV.setText("Requested: " + String.valueOf(workOrder.getRequestDate()));
        //TODO: calculate distance from shoveller address and add here
        //holder.distanceTV.setText(workOrder.getDistance);

        holder.sqFootageTV.setText("Job size: " + String.valueOf(workOrder.getSquareFootage()) + "square feet");
        holder.statusTV.setText("Job Status: " + workOrder.getStatus());
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    public void updateData(List<WorkOrder> newWorkOrders) {
        workOrders.clear();
        workOrders.addAll(newWorkOrders);
        notifyDataSetChanged();
    }
}

