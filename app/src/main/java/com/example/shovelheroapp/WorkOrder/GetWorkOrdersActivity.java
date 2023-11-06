package com.example.shovelheroapp.WorkOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.shovelheroapp.R;

import java.util.List;

public class GetWorkOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private List<WorkOrder> workOrders;
    private WorkOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_work_orders);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize DBHelper and get WO list
        workOrders = WODatabaseHelper.getAllWorkOrders();

        adapter = new WorkOrderAdapter(workOrders);
        recyclerView.setAdapter(adapter);
    }
}