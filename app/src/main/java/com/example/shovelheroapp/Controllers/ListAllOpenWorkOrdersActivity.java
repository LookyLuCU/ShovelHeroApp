package com.example.shovelheroapp.Controllers;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListAllOpenWorkOrdersActivity extends AppCompatActivity {

    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapterForShoveler adapter;
    private List<WorkOrder> workOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_list_all_open);

        workOrderRecyclerView = findViewById(R.id.rvWorkOrders);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        workOrders = new ArrayList<>();
        adapter = new WorkOrderAdapterForShoveler(this, workOrders);
        workOrderRecyclerView.setAdapter(adapter);
        Log.d("ListAllOpenWorkOrders", "onCreate: Started");

        DatabaseReference workOrderReference = FirebaseDatabase.getInstance().getReference("workorders");

        //TODO: change to "open" once work orders working properly
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                workOrders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WorkOrder workOrder = snapshot.getValue(WorkOrder.class);
                    if (workOrder.getStatus().equals("Started")) {
                        workOrders.add(workOrder);
                    }
                }
                Log.d("ListAllOpenWorkOrders", "Data size: " + workOrders.size());
                adapter.notifyDataSetChanged();
                workOrderRecyclerView.setAdapter(adapter);
                Log.d("ListAllOpenWorkOrders", "Adapter notified of data change");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListAllOpenWorkOrders", "Error fetching data: " + error.getMessage());
                error.toException().printStackTrace(); // Print stack trace for detailed error info
            }
        });
    }
}