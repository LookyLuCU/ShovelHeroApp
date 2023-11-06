package com.example.shovelheroapp.WorkOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shovelheroapp.R;

public class CreateWorkOrderActivity extends AppCompatActivity {

    private EditText orderTypeEditText;
    private EditText statusEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work_order);

        orderTypeEditText = findViewById(R.id.editTextOrderType);
        statusEditText = findViewById(R.id.editTextStatus);
    }

    public void createWorkOrder(View view) {
        String orderType = orderTypeEditText.getText().toString();
        String status = statusEditText.getText().toString();


        //create WO object and save to DB
        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderType(orderType);
        workOrder.setStatus(status);

        //save WO to DB
        long newWorkOrderId = WODatabaseHelper.createWorkOrder(workOrder);

        if (newWorkOrderId != -1) {
            Toast.makeText(this, "Work Order created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to create Work Order", Toast.LENGTH_SHORT).show();
        }

        finish();

    }
}