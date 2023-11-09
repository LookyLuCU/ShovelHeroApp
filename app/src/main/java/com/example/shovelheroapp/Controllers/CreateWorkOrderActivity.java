package com.example.shovelheroapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.autofill.AutofillId;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.Enums.Status;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.example.shovelheroapp.Models.WorkOrder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateWorkOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateWorkOrderActivity";
    private Spinner addressSpinner;
    private EditText squareFootageEditText; //-->This should be from AddressId
    private EditText customerShovelerEditText; //-->This should be from AddressId
    private TextView workOrderPriceTextView;
    private Date requestDate;
    private DatePicker requestedDate;
    private TimePicker requestedTime;

    private List<String> itemsRequested;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;

    private EditText specialInstructionsEditText;
    private String status;

    private int iterator;
    private int workOrderID;

    private Double wOPrice;
    private User currentUser;
    private int customerId;
    private int addressId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work_order);

        //bring over customer username
        Intent intent = getIntent();
        if (intent != null) {
            int currentCustomerId = intent.getIntExtra("USER_ID", customerId);
            if (currentCustomerId != 0) {

                final int customerId = currentCustomerId;

                addressSpinner = findViewById(R.id.spinnerAddress);

                //autofilled
                squareFootageEditText = findViewById(R.id.etSquareFootage);
                customerShovelerEditText = findViewById(R.id.etCustomShoveller);

                drivewayCheckBox = findViewById(R.id.cbDriveway);
                walkwayCheckBox = findViewById(R.id.cbWalkway);
                sidewalkCheckBox = findViewById(R.id.sidewalk_checkbox);

                requestedDate = findViewById(R.id.dpCustomDate);
                requestedTime = findViewById(R.id.tpCustomTime);
            }
        }
    }


    public void createWorkOrder(View view) {

        //initiatize ShovelHero DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference workOrderReference = database.getReference("workorders");
        DatabaseReference usersReference = database.getReference("users");
        DatabaseReference addressesReference = database.getReference("addresses");


        //save fields to proper input type
        int customerId = customerId;
        requestDate = Calendar.getInstance().getTime();

        long addressSelection = addressSpinner.getSelectedItemId();
        int addressId = (int) addressSelection;

        String address = addressSpinner.toString();
        String status = Status.Open.toString();
        int sqrFootage = Integer.parseInt(squareFootageEditText.getText().toString());

        //logic for requested date?
        String customerRequestedDate = requestedDate.toString();
        String customerRequestedTime = requestedTime.toString();


        //Order complexity and pricing
        if(drivewayCheckBox.isChecked()){
            itemsRequested.add("Driveway");

            if(currentUser.getAddressId() == addressId && Address currentAddress
                    sqrFootage <= 600) {

                wOPrice = 20.00);

            } else if (walkwayCheckBox.isChecked()){
                itemsRequested.add(("Walkway"));
            } else if (sidewalkCheckBox.isChecked()){
                itemsRequested.add("Sidewalk");
            }
        }


        //create WO object and save to DB
        WorkOrder workOrder = new WorkOrder(workOrderID, requestedDate, status, sq );
        workOrderReference.child(workOrder.getWorkOrderId()).setValue(workOrder);
        //workOrder.setOrderType(orderType);
        workOrder.push().setValue(workOrder);

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


            workOrderPriceTextView.setText("Work Order Price: $" + workOrderPrice);
        } catch (NumberFormatException e) {
            workOrderPriceTextView.setText("Work Order Price: Invalid input");
        }
    }
}