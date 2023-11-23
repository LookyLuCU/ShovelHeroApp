package com.example.shovelheroapp.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shovelheroapp.Models.Enums.Status;
import com.example.shovelheroapp.Models.WorkOrder;
import com.example.shovelheroapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateWorkOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateWorkOrderActivity";


    //initialized database tables
    private DatabaseReference workOrderTable;
    private DatabaseReference workOrderReference;
    private DatabaseReference userTable;

    //address spinner
    //private Spinner addressSpinner;
    //private List<Address> spinnerItemList;
    //private ArrayAdapter<String> spinnerAdapter;


    private String workOrderId;
    private TextView addressTextView;
    private TextView sqFootageTextView;
    //private EditText squareFootageEditText; //-->This should be from AddressId
    private EditText customShovelerEditText;

    private TextView workOrderPriceTextView;


    //Date fields
    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private TextView requestDate;   //date request was placed
    private EditText requestedDate; //date customer has requested in the future
    private TextClock requestedTime;  //time customer has requested in the future


    //WO Items and pricing
    private List<String> itemsRequested;
    private List<CheckBox> itemCheckBoxList;
    private CheckBox drivewayCheckBox;
    private CheckBox walkwayCheckBox;
    private CheckBox sidewalkCheckBox;
    private Double wOPrice = 2.00;  //app overhead cost - maybe more with stripe fee

    private EditText addressNotesEditText;
    TextView dateTimeInLongTextView;



    //Buttons
    private Button btnOrderShovelling;
    private Button btnCancelOrder;


    //instantiated Objects
    private WorkOrder currentWorkOrder;
    private String currentWOId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_create);

        //String dateTime;
        //Calendar calendar;
        //SimpleDateFormat simpleDateFormat;

        addressTextView = findViewById(R.id.tvAddress);
        sqFootageTextView = findViewById(R.id.tvSquareFootage);
        requestDate = findViewById(R.id.tvRequestDate);
        customShovelerEditText = findViewById(R.id.etCustomShoveller);
        addressNotesEditText = findViewById(R.id.etAddressNotes);
        workOrderPriceTextView = findViewById(R.id.tvWorkOrderPrice);

        requestedDate = findViewById(R.id.etCustomDate);
        requestedTime = findViewById(R.id.tpCustomTime);

        calendar = Calendar.getInstance();
        //simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm aaa z");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        //requestedDate.setText(dateTime);

        itemCheckBoxList = new ArrayList<>();
        itemCheckBoxList.add(findViewById(R.id.cbDriveway));
        itemCheckBoxList.add(findViewById(R.id.cbSidewalk));
        itemCheckBoxList.add(findViewById(R.id.cbWalkway));

        btnOrderShovelling = findViewById(R.id.btnOrderShovelling);
        btnCancelOrder = findViewById(R.id.btnCancel);


        Intent intent = getIntent();
        if (intent != null) {
            currentWOId = intent.getStringExtra("WORK_ORDER_ID");
            if (currentWOId != null) {
                System.out.println("work order ID recieved: " + currentWOId);  //WORKING
                retrieveWOInfo(currentWOId);
            }
        }

        System.out.println("Work order id intent ok from profile: " + currentWOId);

        //initialize Firebase
        userTable = FirebaseDatabase.getInstance().getReference("users");

        updateBill();

        //Add checkbox listeners
        for (final CheckBox checkbox : itemCheckBoxList) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    updateBill();
                }
            });
        }
    }


    private void retrieveWOInfo(String currentWOID) {
        System.out.println("Work order received to retrieve wo info: " + currentWOID);

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentWorkOrder = snapshot.getValue(WorkOrder.class);

                    if (currentWorkOrder != null) {
                        //display inital WO information
                        System.out.println("Reading address from Firebase");
                        readAddressFromFirebase(currentWorkOrder);

                        workOrderPriceTextView.setText("Shovelling Price: $2.00");
                        sqFootageTextView.setText("Job Size: " + currentWorkOrder.getSquareFootage() + " square feet");
                        requestDate.setText("Request Date: " + currentWorkOrder.getRequestDate().toString());

                        System.out.println("Work order data loaded");


                        //*******
                        //WORK ORDER CREATE BUTTONS
                        //*******

                        btnOrderShovelling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                releaseWorkOrder(currentWOID);
                            }
                        });

                        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //releaseWorkOrder(currentWOID);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }


    private void readAddressFromFirebase(WorkOrder workOrder) {
        System.out.println("workOrderId received to retrieve address: " + workOrder);

        DatabaseReference addressReference = userTable.child(workOrder.getCustomerId()).child("addresses").child(workOrder.getCustomerAddressId());
        System.out.println("addressed referenced for address, ID: " + addressReference.getKey()); //RETRIEVES OK

        addressReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> addressMap = (HashMap<String, Object>) snapshot.getValue();

                    //retrieve values from hashmap
                    String address = (String) addressMap.get("address");
                    String city = (String) addressMap.get("city");
                    String province = (String) addressMap.get("province");

                    //currentAddress = snapshot.getValue(Address.class);
                    System.out.println("address object recieved from snapshot reading address from firebase: " + address);

                    addressTextView.setText(address + ", " + city + ", " + province);
                        System.out.println("Address retrieved from Firebase: " + address);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }


    //**TODO** - to get into updated form
    private void updatePrice(String currentWOId) {

        System.out.println("Price to be updated");

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            int squareFootage;

            //wOPrice;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentWorkOrder = snapshot.getValue(WorkOrder.class);

                    if (currentWorkOrder != null) {
                        squareFootage = currentWorkOrder.getSquareFootage();

                        //if statements
                        if (drivewayCheckBox.isChecked()) {
                            //itemsRequested.add("Driveway");
                            if (squareFootage <= 600) {
                                wOPrice = wOPrice + 20.00;
                            } else {
                                double pricePerSquareFoot = 0.06;

                                wOPrice = squareFootage * pricePerSquareFoot;
                                System.out.println("Driveway price added, total: " + wOPrice);
                            }
                        }

                        if (sidewalkCheckBox.isChecked()) {
                            //itemsRequested.add("Sidewalk");
                            wOPrice= wOPrice + 10.00;
                            System.out.println("Sidewalk price added, total: " + wOPrice);
                        }

                        if (walkwayCheckBox.isChecked()) {
                            //itemsRequested.add("Walkway");
                            //updatedPrice = wOPrice + walkwayPrice
                            wOPrice = wOPrice + 10.00;
                            System.out.println("Walkway price added, total: " + wOPrice);
                        }
                        workOrderPriceTextView.setText("Shovelling Price $" + wOPrice);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }

    private void updateBill(){
        double totalPrice = wOPrice;

        //iterate through checkboxes
        for (CheckBox checkBox : itemCheckBoxList) {
            if(checkBox.isChecked()){
                totalPrice += getPriceForCheckBoxes(checkBox);
            }
        }
        workOrderPriceTextView.setText("Shovelling Price: $" + totalPrice);
    }


    private Double getPriceForCheckBoxes(CheckBox checkBox){
        if(checkBox.getId() == R.id.cbDriveway) {
            return 20.00;
        } else if (checkBox.getId() == R.id.cbSidewalk) {
            return 10.00;
        } else if (checkBox.getId() == R.id.cbWalkway) {
            return 10.00;
        }
        return 0.00;
    }


    public void releaseWorkOrder(String currentWOId) {
        if (drivewayCheckBox.isChecked() || sidewalkCheckBox.isChecked() || walkwayCheckBox.isChecked()) {
            addWorkOrderDetails(currentWOId);
        } else {
            Toast.makeText(CreateWorkOrderActivity.this, "Please add shovelling area", Toast.LENGTH_SHORT).show();
        }
    }


    public void addWorkOrderDetails(String currentWOId) {
        String customerRequestedDate = requestedDate.getText().toString();
        String customerRequestedTime = requestedTime.getFormat12Hour().toString();
        String status = Status.Open.toString();
        String price = workOrderPriceTextView.getText().toString();

        Map<String, Object> updateWorkOrder = new HashMap<>();

        //update status to "Open", price
        updateWorkOrder.put("status", status);
        updateWorkOrder.put("price", price);

        //Specific Date/Time Requests
        if (customerRequestedDate.isEmpty()) {
            customerRequestedDate.isEmpty();
        } else {
            updateWorkOrder.put("requestedDate", requestedDate);
        }

        if (customerRequestedTime.isEmpty()) {
            customerRequestedTime.isEmpty();
        } else {
            updateWorkOrder.put("requestedTime", requestedTime);
        }


        //Add items Requested List
        if (drivewayCheckBox.isChecked()) {
            itemsRequested.add("Driveway");
            /**
             if (sqrFootage <= 600) {
             wOPrice = wOPrice + 20.00;
             } else {
             double pricePerSquareFoot = 0.06;
             wOPrice = sqrFootage * pricePerSquareFoot;
             }
             **/
        }
        if (sidewalkCheckBox.isChecked()) {
            itemsRequested.add("Sidewalk");
            /**
             wOPrice = wOPrice + 10.00;
             **/
        }
        if (walkwayCheckBox.isChecked()) {
            itemsRequested.add("Walkway");
            /**
             wOPrice = wOPrice + 10.00;
             **/
        }
        updateWorkOrder.put("itemsRequested", itemsRequested);

        workOrderReference = FirebaseDatabase.getInstance().getReference("workorders").child(currentWOId);
        workOrderReference.updateChildren(updateWorkOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Shovelling Request is now active", Toast.LENGTH_SHORT).show();

                        Intent intentCreateWO = new Intent(CreateWorkOrderActivity.this, CustomerProfileActivity.class);
                        String wOID = currentWOId;
                        intentCreateWO.putExtra("WORK_ORDER_ID", wOID);
                        startActivity(intentCreateWO);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkOrderActivity.this, "Could not create Shovelling Job", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
