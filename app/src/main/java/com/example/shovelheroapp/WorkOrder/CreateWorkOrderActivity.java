package com.example.shovelheroapp.WorkOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shovelheroapp.R;
import com.example.shovelheroapp.User.Shoveller.Shoveller;

public class CreateWorkOrderActivity extends AppCompatActivity {

    private EditText orderTypeEditText;
    private EditText statusEditText;
    private EditText squareFootageEditText;
    private EditText pricePerSquareFootEditText;
    private TextView workOrderPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work_order);

        orderTypeEditText = findViewById(R.id.editTextOrderType);
        statusEditText = findViewById(R.id.editTextStatus);

        squareFootageEditText = findViewById(R.id.squareFootageEditText);
        pricePerSquareFootEditText = findViewById(R.id.pricePerSquareFootEditText);
        workOrderPriceTextView = findViewById(R.id.workOrderPriceTextView);
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

    // Create a TextWatcher to calculate the work order price
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculateWorkOrderPrice();
        }
    };


    //**TO UPDATE PRICE based on accepted shoveller type
    //TO LINK SQUARE FOOTAGE TO ADDRESS ID SQ FOOTAGE

    // Calculate and display the work order price
    private void calculateWorkOrderPrice() {
        try {
            double squareFootage = Double.parseDouble(squareFootageEditText.getText().toString()); //-->from address id
            double workOrderPrice = 0.00;

            //youth flat rate for <= 600 sq ft driveway (.06 cents/sqft for bigger jobs)
            //to add later IF SHOVELLER = YOUTH
            if(squareFootage <= 600){
                workOrderPrice = 0.05;
            } else {
                double pricePerSquareFoot = 0.06;
                workOrderPrice = squareFootage * pricePerSquareFoot;
            }

            //adult 0.06 cents/sqft
            //if(Shoveller.class != youth){
            //    double pricePerSquareFoot = 0.08;
            //    double workOrderPrice = squareFootage * pricePerSquareFoot;
            //}

            workOrderPriceTextView.setText("Work Order Price: $" + workOrderPrice);
        } catch (NumberFormatException e) {
            workOrderPriceTextView.setText("Work Order Price: Invalid input");
        }
    }
}