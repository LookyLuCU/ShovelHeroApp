package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.Models.Enums.Status;
import com.example.shovelheroapp.Models.User;
import com.example.shovelheroapp.R;
import com.example.shovelheroapp.Models.WorkOrder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
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
    private EditText requestedDate;
    private TextClock requestedTime;

    private List<String> itemsRequested;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;

    private EditText specialInstructionsEditText;
    private String status;
    TextView dateTimeInLongTextView;

    private int iterator;
    private int workOrderID;

    private Double wOPrice = 0.0;
    private int customerId;
    private int addressId;


    private User currentUser;
    private Address currentAddress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_create);

        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

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

                requestedDate = findViewById(R.id.etCustomDate);
                requestedTime = findViewById(R.id.tpCustomTime);

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
                dateTime = simpleDateFormat.format(calendar.getTime()).toString();
                requestedDate.setText(dateTime);
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
        requestDate = Calendar.getInstance().getTime();

        long addressSelection = addressSpinner.getSelectedItemId();
        int addressId = (int) addressSelection;

        String address = addressSpinner.toString();
        String status = Status.Open.toString();
        int sqrFootage = Integer.parseInt(squareFootageEditText.getText().toString());

        String customerRequestedDate = requestedDate.toString();
        String customerRequestedTime = requestedTime.toString();


        //ORDER COMLPEXITY AND PRICING
        if(drivewayCheckBox.isChecked()) {
            itemsRequested.add("Driveway");
            if (currentAddress.getDrivewaySquareFootage() <= 600) {
                wOPrice = wOPrice + 20.00;
            } else {
                double pricePerSquareFoot = 0.06;
                wOPrice = currentAddress.getDrivewaySquareFootage() * pricePerSquareFoot;
            }
        }

        if(sidewalkCheckBox.isChecked()) {
            itemsRequested.add("Sidewalk");
                wOPrice = wOPrice + 10.00;
            }

        if(walkwayCheckBox.isChecked()) {
            itemsRequested.add("Walkway");
            wOPrice = wOPrice + 10.00;
        }


        //create WO object and save to DB
        WorkOrder newWorkOrder = new WorkOrder(workOrderID, requestDate, status, currentUser.getUserId(), itemsRequested, currentUser.getUserId(), currentUser.getAddressId());
        workOrderReference.child("workorders").setValue(newWorkOrder);

        String accountType = currentUser.getAccountType().toString();
        int currentCustomerId = currentUser.getUserId();

        workOrderReference.push().setValue(newWorkOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(CreateWorkOrderActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        //TO ADD FUNDRAISER AND ADULT SHOVELLER IN LATER ITERATIONS

                        switch (accountType) {
                            case "Youth Shoveller":
                                Intent intentYouth = new Intent(CreateWorkOrderActivity.this, YouthShovelerProfileActivity.class);
                                int youthID = currentCustomerId;
                                intentYouth.putExtra("USER_ID", youthID);
                                startActivity(intentYouth);
                                break;
                            case "Customer":
                                Intent intentCustomer = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                                int customerId = currentCustomerId;
                                intentCustomer.putExtra("USER_ID", customerId);
                                startActivity(intentCustomer);
                                break;
                            case "Guardian":
                                Intent intentGuardian = new Intent(CreateWorkOrderActivity.this, GuardianProfileActivity.class);
                                int guardianId = currentCustomerId;
                                intentGuardian.putExtra("USER_ID", guardianId);
                                startActivity(intentGuardian);
                            default:
                                Intent intent = new Intent(CreateWorkOrderActivity.this, UserRegistrationActivity.class);
                                startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Could not create user. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}