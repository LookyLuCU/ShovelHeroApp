package com.example.shovelheroapp.Controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;

import java.util.List;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder> {
    private List<WorkOrder> workOrders;

    public WorkOrderAdapter(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkOrder workOrder = workOrders.get(position);
        holder.orderTypeTextView.setText(workOrder.getOrderType());
        holder.statusTextView.setText(workOrder.getStatus());
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderTypeTextView;
        TextView statusTextView;

        public ViewHolder(View view) {
            super(view);
            orderTypeTextView = view.findViewById(R.id.orderTypeTextView);
            statusTextView = view.findViewById(R.id.statusTextView);
        }
    }
}

